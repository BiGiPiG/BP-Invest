import { ref } from 'vue'
import { useRouter } from 'vue-router'

const useOAuth2 = () => {
    const router = useRouter()
    const isLoading = ref(false)
    const error = ref(null)

    const generateCodeVerifier = () => {
        const charset = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-._~'
        const length = 128
        let verifier = ''
        for (let i = 0; i < length; i++) {
            verifier += charset.charAt(Math.floor(Math.random() * charset.length))
        }
        return verifier
    }

    const generateCodeChallenge = async (verifier) => {
        const encoder = new TextEncoder()
        const data = encoder.encode(verifier)
        const digest = await window.crypto.subtle.digest('SHA-256', data)
        const base64 = btoa(String.fromCharCode(...new Uint8Array(digest)))
        return base64
            .replace(/\+/g, '-')
            .replace(/\//g, '_')
            .replace(/=+$/, '')
    }

    const loginWithGoogle = async () => {
        isLoading.value = true
        error.value = null

        try {
            const codeVerifier = generateCodeVerifier()
            const codeChallenge = await generateCodeChallenge(codeVerifier)

            sessionStorage.setItem('code_verifier', codeVerifier)

            const params = new URLSearchParams({
                response_type: 'code',
                client_id: '443777150576-pio85oba86ojoijjp6jnmemuv17ja7m3.apps.googleusercontent.com', // ← замени!
                redirect_uri: 'http://localhost:5173/auth/callback',
                scope: 'openid email profile',
                code_challenge: codeChallenge,
                code_challenge_method: 'S256',
                state: Math.random().toString(36).substring(2, 15)
            })

            window.location.href = `https://accounts.google.com/o/oauth2/v2/auth?${params.toString()}`
        } catch (err) {
            error.value = 'Failed to start Google login'
            isLoading.value = false
        }
    }

    // Обмен code на JWT (вызывается в AuthCallback.vue)
    const exchangeCodeForToken = async (code) => {
        isLoading.value = true
        error.value = null

        try {
            const codeVerifier = sessionStorage.getItem('code_verifier')
            if (!codeVerifier) {
                throw new Error('No code_verifier found')
            }

            const response = await fetch('http://localhost:9001/api/auth/exchange-code', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    code,
                    code_verifier: codeVerifier,
                    redirect_uri: 'http://localhost:5173/auth/callback'
                })
            })

            if (!response.ok) {
                throw new Error('Failed to exchange code')
            }

            const data = await response.json()

            if (data.token) {
                localStorage.setItem('token', data.token)
                sessionStorage.removeItem('code_verifier') // очищаем
                router.push('/bp-invest')
            } else {
                throw new Error('No token received')
            }
        } catch (err) {
            error.value = err.message
            isLoading.value = false
            router.push('/login')
        }
    }

    return {
        loginWithGoogle,
        exchangeCodeForToken,
        isLoading,
        error
    }
}

export default useOAuth2