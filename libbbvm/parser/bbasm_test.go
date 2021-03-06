package parser

import (
	"fmt"
	"github.com/davecgh/go-spew/spew"
	"github.com/juju/errors"
	"github.com/stretchr/testify/assert"
	"github.com/wenerme/bbvm/libbbvm/asm"
	"io/ioutil"
	"log"
	"os"
	"path"
	"strconv"
	"strings"
	"testing"
)

func TestTypeMatch(t *testing.T) {
	assert := assert.New(t)

	i, e := strconv.ParseInt("-10", 10, 32)
	assert.NoError(e)
	fmt.Println(i, int32(i))

}

func TestBBAsm(t *testing.T) {
	b, e := ioutil.ReadFile("testdata/exp.txt")
	if e != nil {
		panic(e)
	}
	code := string(b)
	p := &BBAsm{Buffer: code}
	p.Init()
	if err := p.Parse(); err != nil {
		for i, c := range strings.Split(code, "\n") {
			if trim(c) == "" {
				continue
			}
			p := &BBAsm{Buffer: c}
			p.Init()
			e := p.Parse()
			if e != nil {
				panic(errors.Annotatef(e, "Pase failed line %v : %v", i+1, c))
			}
			p.Execute()
		}
	}
	p.PrintSyntaxTree()

	func() {
		defer func() {
			if e := recover(); e != nil {
				spew.Dump(p.stack)
				fmt.Println("----------------------------------------------")
				for _, a := range p.assemblies {
					fmt.Println(a.Assembly())
				}
				if e, ok := e.(error); ok {
					panic(errors.ErrorStack(e.(error)))
				} else {
					panic(e)
				}
			}

		}()
		p.Execute()
	}()

	spew.Dump(p.stack)
	for _, a := range p.assemblies {
		fmt.Println(a.Assembly())
	}
}

func TestLK(t *testing.T) {
	fmt.Println(asm.Lookup(asm.T_INT, "int"))
}

func TestParseCase(t *testing.T) {
	log.SetFlags(log.Ltime | log.Llongfile)
	log.SetOutput(os.Stderr)
	assert := assert.New(t)
	parseWholeDir("../testdata/case", assert)
}

func parseWholeDir(dir string, assert *assert.Assertions) {
	f, e := os.Open(dir)
	assert.NoError(e)
	fi, e := f.Readdir(-1)
	assert.NoError(e)
	for _, f := range fi {
		if f.IsDir() {
			parseWholeDir(path.Join(dir, f.Name()), assert)
		} else if strings.HasSuffix(f.Name(), ".basm") {
			if strings.Contains(f.Name(), "=") {
				fmt.Printf("Ignore %v case\n", path.Join(dir, f.Name()))
			} else {
				testParse(path.Join(dir, f.Name()), assert)
			}
		}

	}
}
func testParse(f string, assert *assert.Assertions) {
	fmt.Println("Parse ", f)
	b, e := ioutil.ReadFile(f)
	assert.NoError(e)
	p := &BBAsm{Buffer: string(b)}
	func() {
		defer func() {
			if e := recover(); e != nil {
				spew.Dump(p.stack)
				fmt.Println("-------------------- PARSE FAILED --------------------------")
				for _, a := range p.assemblies {
					fmt.Println(a.Assembly())
				}
				if e, ok := e.(error); ok {
					panic(errors.ErrorStack(e.(error)))
				} else {
					panic(e)
				}
			}

		}()

		p.Init()
		if err := p.Parse(); err != nil {
			panic(err)
		}
		p.Execute()
	}()
}
