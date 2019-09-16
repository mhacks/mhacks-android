package org.mhacks.app.data.network.registration

import org.mhacks.app.data.network.services.RegistrationService

class RegistrationRepository(
        private val registrationService: RegistrationService,
        private val registrationPrefProvider: RegistrationPrefProvider) {

    // Should not be intercepted and send auth header.
    fun postFirebaseToken(pushId: String) =
            registrationService.postFireBaseToken(pushId, "")

    fun persistAuthToken(authToken: String) {
        registrationPrefProvider.authToken = authToken
    }

    fun getAuthTokenFromCache(): String {
        return registrationPrefProvider.authToken
    }

    fun persistSentTokenToServer(sentTokenToServer: Boolean) {
        registrationPrefProvider.sentTokenToServer = sentTokenToServer
    }


}