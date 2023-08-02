# Introduction

The entry point for KtSoup is the `KtSoupParser` object, it provides all methods for producing `KtSoupDocuments` from
various HTML document sources.

## Parsing

KtSoup provides various methods for parsing HTML documents depending on your use-case.
Regardless of the document source, a `KtSoupDocument` will be returned to traverse the DOM.

### String

```kotlin
val html = "<html><body><h1>Hello, KtSoup!</h1></body></html>"
val document: KtSoupDocument = KtSoupParser.parse(html)
```

### URL

When using the `ktsoup-ktor` module, you'll have access to the `parseRemote(urlString: String)` function.

```kotlin
val document: KtSoupDocument = KtSoupParser.parseRemote("https://duckduckgo.com")
```

### File

When using the `ktsoup-fs` module, you'll have access to the `parseFile(path: String)` function.
Note that all targets except Browser Javascript are supported.

```kotlin
val document: KtSoupDocument = KtSoupParser.parseFile("/path/to/document.html")
```

## Document Usage

Once you've obtained a `KtSoupDocument`, you can use and pass it around including any `KtSoupElements` obtained with it.
When you are done with the document it is important to call `close()` on it to free all resources.

```kotlin
val document: KtSoupDocument = KtSoupParser.parse(html)
// ... Select some elements and read data ...
document.close()
// Document is no longer usable and should be discarded.
```

For simplicity the `use { document -> }` method is provided to automatically free its resources when completed.

```kotlin
val document: KtSoupDocument = KtSoupParser.parse(html)

val title = document.use { document ->
    document.title()
}
```

## Extracting Data

### Document

The `KtSoupDocument` object provides a few specialized functions to simplify accessing Document level details.

#### Head

You can quickly access the `<head>` element using `head()`.

```kotlin
val head: KtSoupElement? = document.head()
```

#### Body

Similar to the document head, the `<body>` element can be accessed with `body()`

```kotlin
val body: KtSoupDocument? = document.body()
```

#### Title

If the document provides a `<title>`, it can be accessed with `title()`.
Note that if a title is not provided, an empty string will be returned.

```kotlin
val title: String = document.title()
```

### Selecting Elements

KtSoup provides CSS query selectors and standard DOM selectors to quickly find single elements or list of matching
elements.

#### CSS Selectors

CSS Selector APIs are available on both `KtSoupDocument`s and `KtSoupElement`s.

##### Single Element

```kotlin
val div: KtSoupElement? = document.querySelector("div#my-div")

val childLink: KtSoupElement? = div?.querySelector("a.my-link")
```

##### Element Lists

```kotlin
val divs: List<KtSoupElement> = document.querySelectorAll("div")

val childLinks: List<KtSoupElement> = divs.flatMap { it.querySelectorAll("a") }
```

#### DOM Selectors

The more basic DOM element selection APIs are available on `KtSoupDocument`s.

```kotlin
val div: KtSoupElement? = document.getElementById("my-div-id")
```

```kotlin
val divs: List<KtSoupElement> = document.getElementsByClass("my-div-class")
```

```kotlin
val divs: List<KtSoupElement> = document.getElementsByTagName("div")
```

## Read Node Data

### Element Attributes

```kotlin
val tagName: String = element.tagName()

// Attribute convenience functions
val elementId: String? = element.id()
val className: String? = element.className()

// Direct attribute value access
val customAttr: String? = element.attr("customAttr")

// Read all attributes
val allAttrs: Map<String, String> = element.attrs()
```

## Modifying Data

**NOTE:** Modification APIs are not currently implemented.

## Examples

For a complete example, see [Scraping Example](3-scraping-example.md).