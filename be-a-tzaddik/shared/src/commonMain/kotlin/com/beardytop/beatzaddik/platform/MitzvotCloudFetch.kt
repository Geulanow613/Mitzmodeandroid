package com.beardytop.beatzaddik.platform

import com.beardytop.beatzaddik.data.MitzvotGeneratorRepository

/** Platform cloud fetch for mitzvotcloud.json (ETag-aware). */
expect suspend fun fetchMitzvotCloudJson(storedEtag: String?): MitzvotGeneratorRepository.CloudFetchResult
