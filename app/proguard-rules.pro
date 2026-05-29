# Keep Moshi adapters
-keep class com.praxis.app.network.models.** { *; }
-keep class com.squareup.moshi.** { *; }

# Keep Hilt generated classes
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }

# Keep Room entities
-keep class com.praxis.app.data.local.** { *; }

# Retrofit
-keepattributes Signature
-keepattributes *Annotation*
-keep class retrofit2.** { *; }
