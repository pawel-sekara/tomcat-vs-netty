package dev.sekara.block.domain.controller

import dev.sekara.block.domain.client.httpbin.HttpBinClient

class ReactiveTestController(
    httpBinClient: HttpBinClient
) : BaseController(httpBinClient)