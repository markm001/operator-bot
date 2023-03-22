package com.ccat.operator.model.entity

data class MemberStatus (
    val total: Int,
    val online: Int,
    val idle: Int,
    val dnd: Int,
    val offline: Int
){ }