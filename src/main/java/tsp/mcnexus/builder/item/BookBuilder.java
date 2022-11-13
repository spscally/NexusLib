package tsp.mcnexus.builder.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class BookBuilder extends ItemBuilder {

    private final BookMeta bookMeta;

    public BookBuilder(ItemStack item) {
        super(item);
        bookMeta = (BookMeta) item.getItemMeta();
    }

    public BookBuilder() {
        this(new ItemStack(Material.BOOK));
    }

    public BookBuilder author(String author) {
        bookMeta.setAuthor(author);
        return this;
    }

    public BookBuilder title(String title) {
        bookMeta.setTitle(title);
        return this;
    }

    public BookBuilder generation(BookMeta.Generation generation) {
        bookMeta.setGeneration(generation);
        return this;
    }

    public BookBuilder addPages(String... pages) {
        bookMeta.addPage(pages);
        return this;
    }

    public BookBuilder setPages(String... pages) {
        bookMeta.setPages(pages);
        return this;
    }

    public BookBuilder page(int index, String page) {
        bookMeta.setPage(index, page);
        return this;
    }

    @Override
    public ItemStack build() {
        setItemMeta(bookMeta);
        return super.build();
    }

}
