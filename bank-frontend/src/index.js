import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import { BrowserRouter } from 'react-router-dom';
import { Provider } from 'react-redux';
import { createStore, applyMiddleware, compose, combineReducers } from 'redux';
import thunk from 'redux-thunk';
import reportWebVitals from './reportWebVitals';
import 'react-redux-toastr/lib/css/react-redux-toastr.min.css';
import { reducer as toastrReducer } from 'react-redux-toastr';
import ReduxToastr from 'react-redux-toastr';
import theme from './theme';
import { ThemeProvider } from '@material-ui/styles';
import merchantListReducer from './components/MerchantAccount/MerchantAccountList/MerchantAccountListReducer';
import clientListReducer from './components/ClientAccount/ClientAccountList/ClientAccountListReducer';

const composeEnhancers = process.env.NODE_ENV === 'development' ? window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ : null || compose;

const rootReducer = combineReducers({
    toastr: toastrReducer,
    merchantList: merchantListReducer,
    clientList: clientListReducer
});

const store = createStore(
  rootReducer,
  composeEnhancers(applyMiddleware(thunk))
);

const app = (
    <Provider store={store}>
        <ThemeProvider theme={theme}>
            <BrowserRouter>
                <App />
            </BrowserRouter>
        </ThemeProvider>
        <ReduxToastr
            timeOut={5000}
            newestOnTop={false}
            preventDuplicates
            position="top-right"
            getState={(state) => state.toastr}
            transitionIn="fadeIn"
            transitionOut="fadeOut"
            progressBar
            closeOnToastrClick />
    </Provider>

);

ReactDOM.render(app, document.getElementById("root"));
reportWebVitals();

