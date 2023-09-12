import { ADD_AUTH } from "./contants";

// 1. initial state
const authLocal = localStorage.getItem("auth_chat");
export const initialState = authLocal ? JSON.parse(authLocal) : null;
console.log(initialState);

// 3. reducer
const reducer = (state, action) => {
    console.log("reducer action : ", action);
    console.log("reducer type : ", action.type);
    console.log("reducer payload : ", action.payload);

    switch (action.type) {
        case ADD_AUTH: {
            state = { ...action.payload }
            localStorage.setItem("auth_chat", JSON.stringify(state));
            return state;
        }
        default: {
            return state;
        }
    }
};

export default reducer;