package com.glints.lingoparents.data.model.response

data class StudentProfileResponse(
    val data: Data? = null,
    val message: String? = null,
    val status: String? = null
) {
    data class Data(
        val level_name: Any? = null,
        val address: String? = null,
        val phone: String? = null,
        val sublevel_name: Any? = null,
        val logo: String? = null,
        val photo: String? = null,
        val id_character: Int? = null,
        val id: Int? = null,
        val fullname: String? = null,
        val email: String? = null
    )
}