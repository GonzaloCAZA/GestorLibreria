// src/context/AuthContext.jsx
import { createContext, useContext, useState, useCallback } from 'react';
import { getMe, logout as apiLogout } from '../api/auth';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [checked, setChecked] = useState(false);

  const checkAuth = useCallback(async () => {
    const result = await getMe();
    if (result.ok) {
      setUser(result.data);
    } else {
      setUser(null);
    }
    setChecked(true);
    return result;
  }, []);

  const logout = useCallback(async () => {
    await apiLogout();
    setUser(null);
  }, []);

  return (
    <AuthContext.Provider value={{ user, setUser, checkAuth, logout, checked }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}
