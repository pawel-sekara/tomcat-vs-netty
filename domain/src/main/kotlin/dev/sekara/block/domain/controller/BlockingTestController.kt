package dev.sekara.block.domain.controller

import dev.sekara.block.domain.client.httpbin.HttpBinClient

class BlockingTestController(
    httpBinClient: HttpBinClient
) : BaseController(httpBinClient)
