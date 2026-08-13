package com.beardytop.beatzaddik.platform

import com.beardytop.beatzaddik.data.MitzvotGeneratorRepository

/** Cloud fetch is optional on iOS for now — bundled list always works offline. */
actual suspend fun fetchMitzvotCloudJson(storedEtag: String?): MitzvotGeneratorRepository.CloudFetchResult =
    MitzvotGeneratorRepository.CloudFetchResult.Unchanged
