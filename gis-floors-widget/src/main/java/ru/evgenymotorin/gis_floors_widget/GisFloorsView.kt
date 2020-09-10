package ru.evgenymotorin.gis_floors_widget

import android.annotation.SuppressLint
import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import org.json.JSONObject
import java.io.Serializable
import java.lang.RuntimeException

@SuppressLint("SetJavaScriptEnabled", "Recycle", "AddJavascriptInterface")
class GisFloorsView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : WebView(context, attributeSet, defStyleAttr) {
    private var configuration: Configuration

    init {
        // get attributes
        configuration =
            context.obtainStyledAttributes(attributeSet, R.styleable.GisFloorsView).let {
                Configuration(
                    complexId = it.getString(R.styleable.GisFloorsView_complexId)
                        ?: throw RuntimeException("complexId is null"),
                    initialFirmId = it.getString(R.styleable.GisFloorsView_initialFirmId) ?: "",
                    hideSearchPanel = it.getBoolean(
                        R.styleable.GisFloorsView_hideSearchPanel,
                        DEFAULT_HIDE_SEARCH_PANEL
                    ),
                    locale = Locales.values()[it.getInt(
                        R.styleable.GisFloorsView_locale,
                        DEFAULT_VALUE_LOCALE.ordinal
                    )],
                    initialZoom = it.getFloat(
                        R.styleable.GisFloorsView_initialZoom,
                        DEFAULT_INITIAL_ZOOM
                    ),
                    maxZoom = it.getFloat(R.styleable.GisFloorsView_maxZoom, DEFAULT_MAX_ZOOM),
                    minZoom = it.getFloat(R.styleable.GisFloorsView_minZoom, DEFAULT_MIN_ZOOM),
                    rotatable = it.getBoolean(
                        R.styleable.GisFloorsView_rotatable,
                        DEFAULT_ROTATABLE
                    ),
                    initialRotation = it.getFloat(
                        R.styleable.GisFloorsView_initialRotation,
                        DEFAULT_INITIAL_ROTATION
                    )
                )
            }

        // configure
        with(settings) {
            setSupportZoom(true)
            javaScriptEnabled = true
            domStorageEnabled = true
            allowUniversalAccessFromFileURLs = true
            allowFileAccessFromFileURLs = true
            allowContentAccess = true
            setAppCacheEnabled(true)
        }
        addJavascriptInterface(MapCallback(), "Callback")

        // load map
        loadUrl(htmlPath)
        webViewClient = GisFloorsWebViewClient()
    }

    fun refresh(configuration: Configuration) {
        this.configuration = configuration
        loadUrl(htmlPath)
    }

    fun getConfiguration() = configuration.copy()

    data class Configuration(
        val complexId: String,
        val initialFirmId: String? = null,
        val hideSearchPanel: Boolean = DEFAULT_HIDE_SEARCH_PANEL,
        val locale: Locales = DEFAULT_VALUE_LOCALE,
        val initialZoom: Float = DEFAULT_INITIAL_ZOOM,
        val maxZoom: Float = DEFAULT_MAX_ZOOM,
        val minZoom: Float = DEFAULT_MIN_ZOOM,
        val rotatable: Boolean = DEFAULT_ROTATABLE,
        val initialRotation: Float = DEFAULT_INITIAL_ROTATION
    ) : Serializable

    enum class Locales(val value: String) { RU("ru_RU"), ES("es_CL"), EN("en_US") }

    private inner class GisFloorsWebViewClient : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            Log.d(TAG, "onPageFinished: $url")
            loadUrl(
                """javascript:loadMap('${configuration.complexId}','${configuration.initialFirmId}',
                    |${configuration.hideSearchPanel},'${configuration.locale.value}',
                    |${configuration.initialZoom},${configuration.minZoom},${configuration.maxZoom},
                    |${configuration.rotatable},${configuration.initialRotation})"""
                    .trimMargin()
            )
        }
    }

    private inner class MapCallback {
        @JavascriptInterface
        fun onFirmSelected(firmIds: String) {
            Log.d(TAG, "onFirmSelected: $firmIds")
        }

        @JavascriptInterface
        fun onFirmLoaded(json: String) {
            Log.d(TAG, "onFirmLoaded: $json")
        }
    }

    companion object {
        private const val htmlPath = "file:///android_asset/complex_map.html"
        private const val TAG = "GisFloorsView"

        private const val DEFAULT_INITIAL_ZOOM = 17f
        private const val DEFAULT_MAX_ZOOM = 19.5f
        private const val DEFAULT_MIN_ZOOM = 17f
        private const val DEFAULT_ROTATABLE = false
        private const val DEFAULT_INITIAL_ROTATION = 0.45f
        private const val DEFAULT_HIDE_SEARCH_PANEL = true
        private val DEFAULT_VALUE_LOCALE = Locales.RU
    }
}