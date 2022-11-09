object Versions {
    const val amplify = "1.36.6-dev-preview.0"
    const val amplify_kotlin = "0.20.6-dev-preview.0"

    const val compose_viewmodel = "2.5.1"
}

object Libs {
    const val amplify_core_kotlin = "com.amplifyframework:core-kotlin:${Versions.amplify_kotlin}"
    const val amplify_aws_api = "com.amplifyframework:aws-api:${Versions.amplify}"
    const val amplify_datastore = "com.amplifyframework:aws-datastore:${Versions.amplify}"

    const val compose_viewmodel =
        "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.compose_viewmodel}"
}