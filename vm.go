package bbvm
import (
	"fmt"
	"math"
)

type InstHandler func(*Inst)
const HANDLE_ALL = math.MaxInt32

type Register interface {
	Get() int
	Set(int)
}
type register struct {
	val int
}
func (r *register)Get() int {
	return r.val
}
func (r *register)Set(v int) {
	r.val = v
}

type monitorRegister struct {
	val int
	Changed bool
}

func (r *monitorRegister)Set(v int) {
	r.val=v
	r.Changed = true
}

func (r *monitorRegister)Get() int {
	return r.val
}


type vm struct {
	mem []byte
	in map[string]InstHandler
	out map[string]InstHandler

	rp monitorRegister
	rf register
	rs register
	rb register
	r0 register
	r1 register
	r2 register
	r3 register

	inst Inst

	exited bool

	stack []byte

	strPool ResPool
}
func (v *vm)Load(rom []byte) {
	log.Info("Load ROM. size: %d", len(rom))
	v.Reset()
	v.mem = make([]byte, len(rom))
	copy(v.mem, rom)
	v.rb.Set(len(v.mem))
	v.rs.Set(v.rb.Get())

	log.Info("Init stack. size: %d", 1024)
	v.mem = append(v.mem, make([]byte, 1024)...)
	v.stack = v.mem[len(rom):]
}
func (v *vm)Reset() {
	v.r0.Set(0)
	v.r1.Set(0)
	v.r2.Set(0)
	v.r3.Set(0)
	v.rs.Set(0)
	v.rb.Set(0)
	v.rp.Set(0)
	v.rf.Set(0)
}
func (v *vm)SetIn(a, b int, h InstHandler) {
	v.in[multiPortKey(a, b)] = h
}
func (v *vm)SetOut(a, b int, h InstHandler) {
	v.out[multiPortKey(a, b)] = h
}
func (v *vm)In(a, b int) InstHandler {
	return multiPortHandler(a, b, v.in)
}
func (v *vm)Out(a, b int) InstHandler {
	return multiPortHandler(a, b, v.out)
}


func (v *vm)Register(t RegisterType) Register {
	switch t{
		case REG_RP: return &v.rp
		case REG_RF: return &v.rf
		case REG_RS: return &v.rs
		case REG_RB: return &v.rb
		case REG_R0: return &v.r0
		case REG_R1: return &v.r1
		case REG_R2: return &v.r2
		case REG_R3: return &v.r3
	}
	return nil
}
func (v *vm)Loop() {
	v.rp.Changed = false

	if v.rp.Get() >= len(v.mem){
		log.Info("Run over, exit")
		v.Exit()
		return
	}

	err := v.inst.UnmarshalBinary(v.mem[v.rp.Get():])
	if err != nil { panic(err)}
	v.Proc()
	if !v.rp.Changed {
		v.rp.Set(v.rp.Get() + v.inst.Opcode.Len())
	}
}

func (v *vm)Report() string {
	s := fmt.Sprintf("# rp:%d rf:%d rs:%d rb:%d r0:%d r1:%d r2:%d r3:%d\n%s",
	v.rp.Get(), v.rf.Get(), v.rs.Get(), v.rb.Get(),
	v.r0.Get(), v.r1.Get(), v.r2.Get(), v.r3.Get(), v.inst)
	return s
}
func (v *vm)Exit() {
	v.exited = true
}
func (v *vm)IsExited() bool {
	return v.exited
}

func (v *vm)StrPool() ResPool {
	return v.strPool
}



func NewVM() *vm {
	v := &vm{
		in:make(map[string]InstHandler),
		out:make(map[string]InstHandler),
	}
	v.strPool = newStrPool()
	v.inst.VM = v
	v.inst.A.Vm = v
	v.inst.B.Vm = v
	return v
}

type VM interface {
	SetOut(int, int, InstHandler)
	SetIn(int, int, InstHandler)
}


