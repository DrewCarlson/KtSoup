# Scraping Example

Here is a complete example of scraping information from a product web page.

## HTML

Let's consider the following well-formed HTML representing an e-commerce item page.

```html
<!DOCTYPE html>
<html>
<head>
    <title>Product Page</title>
</head>
<body>
    <div id="product-info">
        <h1 id="product-name">Cool Product</h1>
        <p id="product-sku">SKU12345</p>
        <p id="product-price">$199.99</p>
        <p id="product-quantity">5 in stock</p>
        <img id="product-image" src="image_url.jpg">
        <p id="product-description">This is a cool product!</p>
        <p id="product-manufacturer">Manufacturer: Cool Products Inc.</p>
    </div>
    <div id="related-products">
        <div class="product">
            <h2 class="product-name">Related Product 1</h2>
            <p class="product-sku">SKU67890</p>
            <p class="product-price">$149.99</p>
        </div>
        <div class="product">
            <h2 class="product-name">Related Product 2</h2>
            <p class="product-sku">SKU11223</p>
            <p class="product-price">$99.99</p>
        </div>
    </div>
</body>
</html>
```

## Setup

First it's a good practice to create models to represent the data we want to extract.

```kotlin
data class Product(
    val name: String,
    val sku: String,
    val price: String,
    val quantity: String,
    val description: String,
    val imageUrl: String,
    val manufacturer: String,
    val relatedProducts: List<RelatedProduct>
)

data class RelatedProduct(
    val name: String,
    val sku: String,
    val price: String
)
```

## Scraping

Next call `parse()` with the HTML document or use `parseRemote(url)` to get a `KtSoupDocument`.
With the document in hand, query all the desired elements and extract the relevant text content.

```kotlin
val product = KtSoupParser.parse(html).use { doc ->
    val relatedProducts = doc.querySelectorAll("#related-products .product").map { product ->
        RelatedProduct(
            name = product.querySelector(".product-name")?.textContent().orEmpty(), 
            sku = product.querySelector(".product-sku")?.textContent().orEmpty(), 
            price = product.querySelector(".product-price")?.textContent().orEmpty(),
        )
    }
    Product(
        name = doc.getElementById("product-name")?.textContent().orEmpty(),
        sku = doc.getElementById("product-sku")?.textContent().orEmpty(),
        price = doc.getElementById("product-price")?.textContent().orEmpty(),
        quantity = doc.getElementById("product-quantity")?.textContent().orEmpty(),
        description = doc.getElementById("product-description")?.textContent().orEmpty(),
        imageUrl = doc.getElementById("product-image")?.attr("src").orEmpty(),
        manufacturer = doc.getElementById("product-manufacturer")?.textContent().orEmpty(),
        relatedProducts = relatedProducts
    )
}
```

## Done!

That's all it takes to scrape the information we wanted!
We can print the `product` to verify we've got all the desired information.

```text
Product(
    name=Cool Product,
    sku=SKU12345,
    price=$199.99,
    quantity=5 in stock,
    description=This is a cool product!,
    imageUrl=image_url.jpg,
    manufacturer=Manufacturer: Cool Products Inc.,
    relatedProducts=[
        RelatedProduct(name=Related Product 1, sku=SKU67890, price=$149.99),
        RelatedProduct(name=Related Product 2, sku=SKU11223, price=$99.99)
    ]
)
```