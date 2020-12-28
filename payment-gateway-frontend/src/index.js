import React from "react";
import ReactDOM from "react-dom";
import "./index.css";
import App from "./App";
import reportWebVitals from "./reportWebVitals";
import { reducer as toastrReducer } from "react-redux-toastr";
import { applyMiddleware, combineReducers, createStore, compose } from "redux";
import thunk from "redux-thunk";
import { BrowserRouter } from "react-router-dom";
import { Provider } from "react-redux";
import 'react-redux-toastr/lib/css/react-redux-toastr.min.css';
import ReduxToastr from 'react-redux-toastr';
import paymentHomeReducer from "./components/PaymentHome/PaymentHomeReducer";
import paymentMethodListReducer from './components/PaymentMethodsManagement/PaymentMethodList/PaymentMethodListReducer';
import signInReducer from './components/Authentication/SignIn/SignInReducer';
import rawTheme from './theme';
import { MuiThemeProvider } from "@material-ui/core/styles";
import addCompanyReducer from "./components/CompanyManagement/AddCompany/AddCompanyReducer";

const composeEnhancers =
  process.env.NODE_ENV === "development"
    ? window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__
    : null || compose;

const rootReducer = combineReducers({
  toastr: toastrReducer,
  paymentHome: paymentHomeReducer,
  paymentMethodList: paymentMethodListReducer,
  signIn: signInReducer,
  addCompany: addCompanyReducer,
});

const store = createStore(
  rootReducer,
  composeEnhancers(applyMiddleware(thunk))
);

const app = (
  <Provider store={store}>
    <BrowserRouter>
      <MuiThemeProvider theme={rawTheme}>
        <App />
      </MuiThemeProvider>
    </BrowserRouter>
    <ReduxToastr
      timeOut={5000}
      newestOnTop={false}
      preventDuplicates
      position="top-right"
      getState={(state) => state.toastr}
      transitionIn="fadeIn"
      transitionOut="fadeOut"
      progressBar
      closeOnToastrClick
    />
  </Provider>
);

ReactDOM.render(app, document.getElementById("root"));

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
