package me.wener.bbvm.vm.res;

/**
 * @author wener
 * @since 15/12/18
 */
public interface ImageManager extends ResourceManager<ImageManager, ImageResource> {
    PageResource load(String file, int index);
}