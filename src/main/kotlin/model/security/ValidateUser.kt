package model.security

import model.security.dao.User
import utils.errors.OnlyUserError
import utils.errors.UnauthorizedError

fun TokenService.validateUserIsUser(token: String): User {
    val cachedUser = token.validateTokenIsLoggedIn(this)

    if (cachedUser.permissions?.contains("user") != true) {
        throw OnlyUserError()
    }

    return cachedUser
}

fun String?.validateTokenIsUserUser(tokenService: TokenService): User {
    this ?: throw UnauthorizedError()
    return tokenService.validateUserIsUser(this)
}
