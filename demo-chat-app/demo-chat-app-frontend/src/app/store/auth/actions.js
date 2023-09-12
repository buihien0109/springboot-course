import { ADD_AUTH } from "./contants"

export const addAuth = (auth) => {
    return {
        type: ADD_AUTH,
        payload: auth
    }
}