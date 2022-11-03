package com.osh.sample.main.impl.domain

import com.osh.sample.domain.repository.PagedRepository

internal interface RecordsRepository : PagedRepository<String, RecordsItem>
