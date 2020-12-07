import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import {BrowserRouter} from 'react-router-dom';
import {Provider} from 'react-redux';
import {createStore,applyMiddleware, compose, combineReducers} from 'redux';
import thunk from 'redux-thunk';
import reportWebVitals from './reportWebVitals';
import signInReducer from './components/Authentication/SignIn/SignInReducer';
import signUpReducer from './components/Authentication/SignUp/SignUpReducer';

// ReactDOM.render(
//   <React.StrictMode>
//     <App />
//   </React.StrictMode>,
//   document.getElementById('root')
// );


const composeEnhancers = process.env.NODE_ENV === 'development'? window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ : null || compose;

const rootReducer = combineReducers({
    signIn: signInReducer,
    signUp: signUpReducer
});

const store = createStore(rootReducer, composeEnhancers(
    applyMiddleware(thunk)
));


const app = (
    <Provider store = {store}>
        <BrowserRouter>
            <App />
        </BrowserRouter>
    </Provider>
   
);

ReactDOM.render(app, document.getElementById('root'));
reportWebVitals();
