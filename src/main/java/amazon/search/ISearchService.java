package amazon.search;

import amazon.Product;

import java.util.List;

public interface ISearchService {

    public List<Product> searchByName(String name);
    public List<Product> searchByCategory(Category productCategory);

}
