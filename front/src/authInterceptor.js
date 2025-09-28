let isRedirecting = false
let pendingRequests = []

export function setupAuthInterceptor(router) {
    const originalFetch = window.fetch

    window.fetch = async function(...args) {
        if (isRedirecting) {
            throw new Error('Authentication redirect in progress')
        }

        try {
            const response = await originalFetch.apply(this, args)

            if (response.status === 401) {
                const errorData = await response.json().catch(() => ({}))
                if (errorData.code === 'TOKEN_EXPIRED') {
                    if (!isRedirecting) {
                        isRedirecting = true
                        localStorage.removeItem('token')

                        pendingRequests.forEach(req => req.abort?.())
                        pendingRequests = []

                        router.push('/login')
                    }
                }
            }

            return response
        } catch (error) {
            if (error.message === 'TOKEN_EXPIRED') {
                throw error // Пробрасываем дальше
            }
            throw error
        }
    }
}