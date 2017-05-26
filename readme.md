World Weather
=============
World Weather merupakan final project dari acara [Bekraf Digital Talent 2017](https://www.kumpul.co/events/bekraf-digital-talent-2017-170501/ "Event Bekraf Digital Talent 2017") yang diadakan pada tanggal 08 May 2017 - 21 May 2017 di [Kumpul Coworking Space](https://goo.gl/maps/dxMyzJbWnax "Maps Kumpul Coworking Space").

Aplikasi ini berfungsi untuk menunjukkan cuaca terkini dari berbagai negara dengan memanfaatkan API yang disediakan.

---

### Teknologi yang digunakan :
* [Android Studio](http://developer.android.com/sdk/index.html "Download Android Studio")
* [Java Development Kit](http://www.oracle.com/technetwork/java/javase/downloads/index.html "Download JDK")
* [Glide - Image loading framework](https://github.com/bumptech/glide "View Glide options on github")
* [AndroidSVG](https://bigbadaboom.github.io/androidsvg/ "Check AndroidSVG documentation")

### Data API :
* [Random User Generator](https://randomuser.me/api/ "Generate random user")
* [REST Countries](https://restcountries.eu/rest/v2/all "Generate all countries data")
* [Open Weather Map](http://openweathermap.org/api "Generate weather data")

### Tools :
* [JSON Formatter](https://jsonformatter.curiousconcept.com/ "Tools untuk memperbaiki format JSON agar mudah dibaca")

---

## Load Gambar SVG :
API [REST Countries](https://restcountries.eu/rest/v2/all "Generate all countries data") memberikan link berupa gambar bendera dalam vector, sedangkan di android tidak mengenali tipe SVG secara default.

Berikut adalah contoh gambar vector :
<kbd>![Negara Kesatuan Republik Indonesia](https://restcountries.eu/data/idn.svg)</kbd>

Dengan memanfaatkan fungsi dari [AndroidSVG](https://bigbadaboom.github.io/androidsvg/ "Check AndroidSVG documentation") dan [Glide - Image loading framework](https://github.com/bumptech/glide "View Glide options on github"), kita dapat menampilkan gambar vector dengan cara seperti ini :

**Tambahkan perintah ini di gradle dependencies :**
```java
    /* panggil androidsvg untuk menampilkan gambar SVG */
    compile 'com.caverock:androidsvg:1.2.1'

    /* panggil glide untuk menampilkan gambar di ImageView dari hyperlink */
    compile 'com.github.bumptech.glide:glide:4.0.0-RC0'
    annotationProcessor 'com.github.bumptech.glide:compiler:4.0.0-RC0'
```

**Tambahkan kode ini di constructor adapter :**
```java
    private RequestBuilder<PictureDrawable> requestBuilder;

    requestBuilder = GlideApp.with(context)
        .as(PictureDrawable.class)
        .placeholder(R.drawable.image_loading)
        .error(R.drawable.image_error)
        .transition(withCrossFade())
        .listener(new SvgSoftwareLayerSetter());

    Uri uri = Uri.parse("https://restcountries.eu/data/idn.svg");
    requestBuilder.load(uri).into(imgFlag);
```

**Salin file berikut dari github [Glide - Image loading framework](https://github.com/bumptech/glide "View Glide options on github") di folder `sample/svg/` :**

**1. Daftar file activity :**
* [Source SvgDecoder](https://github.com/bumptech/glide/blob/master/samples/svg/src/main/java/com/bumptech/glide/samples/svg/SvgDecoder.java "Source SvgDecoder")
* [Source SvgDrawableTranscoder](https://github.com/bumptech/glide/blob/master/samples/svg/src/main/java/com/bumptech/glide/samples/svg/SvgDrawableTranscoder.java "Source SvgDrawableTranscoder")
* [Source SvgModule](https://github.com/bumptech/glide/blob/master/samples/svg/src/main/java/com/bumptech/glide/samples/svg/SvgModule.java "Source SvgModule")
* [Source SvgSoftwareLayerSetter](https://github.com/bumptech/glide/blob/master/samples/svg/src/main/java/com/bumptech/glide/samples/svg/SvgSoftwareLayerSetter.java "Source SvgSoftwareLayerSetter")

**2. Daftar file drawable :**
* [Source dot_dot_dot](https://github.com/bumptech/glide/blob/master/samples/svg/src/main/res/drawable/dot_dot_dot.xml "Source dot_dot_dot")
* [Source image_error](https://github.com/bumptech/glide/blob/master/samples/svg/src/main/res/drawable/image_error.xml "Source image_error")
* [Source image_loading](https://github.com/bumptech/glide/blob/master/samples/svg/src/main/res/drawable/image_loading.xml "Source image_loading")