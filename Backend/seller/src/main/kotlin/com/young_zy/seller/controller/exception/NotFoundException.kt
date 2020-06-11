package com.young_zy.seller.controller.exception

class NotFoundException(override val message: String = "Requested object not found") : Exception()