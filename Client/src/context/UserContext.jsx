import { createContext, useContext, useState } from "react";

// יצירת הקונטקסט
export const UserContext = createContext(null);

// הוק נוח לשימוש בקונטקסט
export const useUserContext = () => useContext(UserContext);

// קומפוננטת Provider שמנהלת את המשתמש
export const UserProvider = ({ children }) => {
  const [user, setUser] = useState(null);

  return (
    <UserContext.Provider value={{ user, setUser }}>
      {children}
    </UserContext.Provider>
  );
};
