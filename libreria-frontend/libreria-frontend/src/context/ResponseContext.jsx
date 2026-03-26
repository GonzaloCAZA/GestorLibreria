// src/context/ResponseContext.jsx
import { createContext, useContext, useState } from 'react';

const ResponseContext = createContext(null);

export function ResponseProvider({ children }) {
  const [response, setResponse] = useState(null);
  return (
    <ResponseContext.Provider value={{ response, setResponse }}>
      {children}
    </ResponseContext.Provider>
  );
}

export function useResponse() {
  return useContext(ResponseContext);
}
