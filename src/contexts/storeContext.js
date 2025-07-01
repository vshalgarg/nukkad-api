import { createContext,useContext, useState } from "react";

const StoreContext= createContext();

export const StoreProvider=({children})=>{
    const [storeData,setStoreData]= useState(null);
    const saveStore=(data)=>{
        setStoreData(data);
    };
    const resetStore=()=>setStoreData([]);
    return(
        <StoreContext.Provider value={{storeData,setStoreData ,saveStore, resetStore  }}>
            {children}
        </StoreContext.Provider>
    )
}
export const useStore=()=>useContext(StoreContext);