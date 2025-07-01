import { createSlice } from "@reduxjs/toolkit";

let initialState={
    userType:null,
}

const userSlice = createSlice({
  name: "user",
  initialState,
  reducers: {
    setUserType(state, action) {
      state.userType = action.payload;
    },
    resetUser: (state) => {
      state.user = null;
    },
  },
});

export const {setUserType,resetUser}= userSlice.actions;
export default userSlice.reducer
