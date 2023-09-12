import React, { useReducer } from 'react';
import reducer, { initialState } from '../store/auth/reducer';
import Context from './Context';

function Provider({ children }) {
    const [auth, dispatchAuth] = useReducer(reducer, initialState);

    return (
        <Context.Provider value={{ auth, dispatchAuth }}>
            {children}
        </Context.Provider>
    )
}

export default Provider