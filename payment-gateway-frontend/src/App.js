import { connect } from 'react-redux';
import "./App.css";
import PaymentHome from "./components/PaymentHome/PaymentHome";
import { Route, withRouter, Switch } from "react-router-dom";
import AddPaymentMethod from "./components/PaymentMethodsManagement/AddPaymentMethod/AddPaymentMethod";
import AddCompany from "./components/CompanyManagement/AddCompany/AddCompany";
import AddPaymentMethodSuccess from './components/PaymentMethodsManagement/AddPaymentMethod/AddPaymentMethodSuccess';
import PaymentMethodList from './components/PaymentMethodsManagement/PaymentMethodList/PaymentMethodList';
import SignIn from './components/Authentication/SignIn/SignIn';
import ChangePassword from './components/Authentication/ChangePassword/ChangePassword';
import * as actions from './components/Authentication/SignIn/SignInExport';
import React, { useEffect, Suspense } from 'react';
import NonAuthenticated from './components/Authentication/Error/NonAuthenticated';
import NonAuthorized from './components/Authentication/Error/NonAuthorized';
import CustomToolbar from './components/Navigation/Toolbar/Toolbar';
import { GuardProvider, GuardedRoute } from 'react-router-guards';
import Logout from './components/Authentication/SignIn/Logout';
import AddCompanySuccess from './components/CompanyManagement/AddCompany/AddCompanySuccess';
import CompanyList from './components/CompanyManagement/CompanyList/CompanyList';
import MerchantSupportPaymentMethods from './components/PaymentMethodsManagement/MerchantSupportPaymentMethods/MerchantSupportPaymentMethods';
const App = (props) => {
  const { onTryAutoSignUp } = props;

  const roleAdmin = "ROLE_ADMIN";

  useEffect(() => {
    onTryAutoSignUp();
  }, [onTryAutoSignUp]);

  const requireRole = (to, from, next) => {
    if (to.meta.roles) {
      to.meta.roles.forEach(element => {
        if (element === props.role) {
          next();
        }
      });
      next.redirect('/error/non-authorized');
    } else {
      next();
    }
  };

  let routes = (
    <Switch>
      <Route path="/sign-in" render={(props) => <SignIn {...props} />} />
      <Route path="/change-password" render={(props) => <ChangePassword {...props} />} />

      <Route path="/error/non-authenticated" render={(props) => <NonAuthenticated {...props} />} />
      <Route path="/error/non-authorized" render={(props) => <NonAuthorized {...props} />} />

      <Route path="/payment/:id" render={(props) => <PaymentHome {...props} />} />

      <Route path="/add-payment-method" render={() => <AddPaymentMethod {...props} />} />
      <Route path="/add-payment-method-success" render={() => <AddPaymentMethodSuccess {...props} />} />

      <Route path="/add-company" render={() => <AddCompany {...props} />} />
      <Route path="/add-company-success" render={() => <AddCompanySuccess {...props} />} />

      <Route path="/payment-data" render={(props) => <MerchantSupportPaymentMethods {...props} />} />

      <Route path="/" render={() => <h1> Welcome, navigate to payment/:id. </h1>} />
    </Switch>
  );

  if (props.isAuthenticated) {
    routes = (
      <GuardProvider guards={[requireRole]}>
        <Switch>
          <Route path="/payment/:id" render={(props) => <PaymentHome {...props} />} />

          <GuardedRoute path="/payment-methods" render={() => <PaymentMethodList {...props} />} meta={{ roles: [roleAdmin] }} />
          <GuardedRoute path="/companies" render={() => <CompanyList {...props} />} meta={{ roles: [roleAdmin] }} />

          <Route path="/add-payment-method" render={() => <AddPaymentMethod {...props} />} />
          <Route path="/add-payment-method-success" render={() => <AddPaymentMethodSuccess {...props} />} />

          <Route path="/add-company" render={() => <AddCompany {...props} />} />
          <Route path="/add-company-success" render={() => <AddCompanySuccess {...props} />} />

          <Route path="/error/non-authorized" render={(props) => <NonAuthorized {...props} />} />

          <Route path="/sign-out" render={(props) => <Logout {...props} />} />
          <Route path="/" render={() => <h1> Welcome </h1>} />
        </Switch>
      </GuardProvider>
    );

  }

  return (
    <div>
      <CustomToolbar role={props.role} isAuthenticated={props.isAuthenticated} />
      <Suspense fallback={<p>Loading...</p>}>{routes}</Suspense>
      <main>
        {props.children}
      </main>
    </div>
  );
};

const mapStateToProps = state => {
  return {
    isAuthenticated: state.signIn.isAuthenticated,
    role: state.signIn.role,
  }
};

const mapDispatchToProps = dispatch => {
  return {
    onTryAutoSignUp: () => dispatch(actions.authCheckState()),
  }
}

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(App));

