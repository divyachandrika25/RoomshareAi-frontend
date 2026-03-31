import React, { createContext, useContext, useState, useEffect } from 'react';

const AppContext = createContext();

export const AppProvider = ({ children }) => {
  const [savedRooms, setSavedRooms] = useState(() => {
    const saved = localStorage.getItem('savedRooms');
    return saved ? JSON.parse(saved) : [];
  });

  useEffect(() => {
    localStorage.setItem('savedRooms', JSON.stringify(savedRooms));
  }, [savedRooms]);

  const toggleSaveRoom = (room) => {
    setSavedRooms((prev) => {
      const isSaved = prev.some((r) => r.id === room.id);
      if (isSaved) {
        return prev.filter((r) => r.id !== room.id);
      } else {
        return [...prev, room];
      }
    });
  };

  const isRoomSaved = (roomId) => savedRooms.some((r) => r.id === roomId);

  return (
    <AppContext.Provider value={{ savedRooms, toggleSaveRoom, isRoomSaved }}>
      {children}
    </AppContext.Provider>
  );
};

export const useAppContext = () => {
  const context = useContext(AppContext);
  if (!context) {
    throw new Error('useAppContext must be used within an AppProvider');
  }
  return context;
};
