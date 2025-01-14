export const auth = {
  async setToken(token: string) {
    await fetch('/api/auth/token', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ token })
    })
  },

  async removeToken() {
    await fetch('/api/auth/token', {
      method: 'DELETE'
    })
  }
}
