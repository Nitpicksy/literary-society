import React from 'react';
import ReactDOM from 'react-dom';
import App from './App';
import './index.css';
import 'typeface-roboto';
import 'react-redux-toastr/lib/css/react-redux-toastr.min.css';
import { reducer as toastrReducer } from 'react-redux-toastr';
import ReduxToastr from 'react-redux-toastr';
import { BrowserRouter } from 'react-router-dom';
import { Provider } from 'react-redux';
import { createStore, applyMiddleware, compose, combineReducers } from 'redux';
import thunk from 'redux-thunk';
import reportWebVitals from './reportWebVitals';
import signInReducer from './components/Authentication/SignIn/SignInReducer';
import signUpReducer from './components/Authentication/SignUp/SignUpReducer';
import resetPasswordReducer from './components/Authentication/ResetPassword/ResetPasswordReducer';
import activateAccountReducer from './components/Authentication/ActivateAccount/ActivateAccountReducer';
import betaReaderGenresReducer from './components/Authentication/BetaReaderGenres/BetaReaderGenresReducer';
import homePageReducer from './components/HomePage/HomePageReducer';
import createPublicationRequestReducer from './components/WriterPages/CreatePublicationRequest/CreatePublicationRequestReducer';

const composeEnhancers = process.env.NODE_ENV === 'development' ? window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ : null || compose;

const rootReducer = combineReducers({
    toastr: toastrReducer,
    signIn: signInReducer,
    signUp: signUpReducer,
    resetPassword: resetPasswordReducer,
    activateAccount: activateAccountReducer,
    betaReaderGenres: betaReaderGenresReducer,
    homePage: homePageReducer,
    createPublicationRequest: createPublicationRequestReducer,
});

const store = createStore(rootReducer, composeEnhancers(
    applyMiddleware(thunk)
));


const app = (
    <Provider store={store}>
        <BrowserRouter>
            <App />
        </BrowserRouter>
        <ReduxToastr
            timeOut={5000}
            newestOnTop={false}
            preventDuplicates
            position="top-right"
            getState={(state) => state.toastr} // This is the default
            transitionIn="fadeIn"
            transitionOut="fadeOut"
            progressBar
            closeOnToastrClick />
    </Provider>

);

ReactDOM.render(app, document.getElementById('root'));
reportWebVitals();
