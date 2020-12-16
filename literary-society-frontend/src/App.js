import './App.css';
import React, { Suspense, useEffect } from 'react';
import { Route, Switch, withRouter, Redirect } from 'react-router-dom';
import { connect } from 'react-redux';
import * as actions from './components/Authentication/SignIn/SignInExport';
import SignIn from './components/Authentication/SignIn/SignIn';
import SignUpOptions from './components/Authentication/SignUpOptions/SignUpOptions';
import SignUp from './components/Authentication/SignUp/SignUp';
import SignUpFinished from './components/Authentication/SignUpFinished/SignUpFinished';
import Logout from './components/Authentication/SignIn/Logout';
import NonAuthenticated from './components/Authentication/Error/NonAuthenticated';
import NonAuthorized from './components/Authentication/Error/NonAuthorized';
import ChangePassword from './components/Authentication/ChangePassword/ChangePassword';
import ResetPasswordEnterNewPass from './components/Authentication/ResetPassword/ResetPasswordEnterNewPass';
import ResetPasswordEnterUsername from './components/Authentication/ResetPassword/ResetPasswordEnterUsername';
import HomePage from './components/HomePage/HomePage';
import CustomToolbar from './components/Navigation/Toolbar/Toolbar';
import PaymentSuccess from './components/Payment/PaymentSuccess';
import PaymentError from './components/Payment/PaymentError';
import PaymentFailed from './components/Payment/PaymentFailed';
import ActivateAccount from './components/Authentication/ActivateAccount/ActivateAccount';
import BetaReaderGenres from './components/Authentication/BetaReaderGenres/BetaReaderGenres';
import ShoppingCart from './components/ShoppingCart/ShoppingCart';
import PublicationRequests from './components/WriterPages/PublicationRequests/PublicationRequests';
import CreatePublicationRequest from './components/WriterPages/CreatePublicationRequest/CreatePublicationRequest';
import Tasks from './components/Tasks/Tasks';
import PublicationRequest from './components/Tasks/Task/PublicationRequest/PublicationRequest';

// const Auth = React.lazy(() => {
//   return import('./containers/Auth/Auth');
// });

const App = props => {

  const { onTryAutoSignUp } = props;

  useEffect(() => {
    onTryAutoSignUp();
  }, [onTryAutoSignUp]);

  let routes = (
    <Switch>
      <Route path="/sign-in" render={(props) => <SignIn {...props} />} />
      <Route path="/sign-up-options" render={(props) => <SignUpOptions {...props} />} />
      <Route path="/sign-up" render={(props) => <SignUp {...props} />} />
      <Route path="/sign-up-finished" render={(props) => <SignUpFinished {...props} />} />
      <Route path="/choose-genres" render={(props) => <BetaReaderGenres {...props} />} />
      <Route path="/change-password" render={(props) => <ChangePassword {...props} />} />
      <Route path="/forgot-password" render={(props) => <ResetPasswordEnterUsername {...props} />} />
      <Route path="/reset-password" render={(props) => <ResetPasswordEnterNewPass {...props} />} />
      <Route path="/activate-account" render={(props) => <ActivateAccount {...props} />} />
      <Route path="/error/non-authenticated" render={(props) => <NonAuthenticated {...props} />} />
      <Route path="/error/non-authorized" render={(props) => <NonAuthorized {...props} />} />
      <Route path="/payment/success/:id" render={(props) => <PaymentSuccess {...props} />} />
      <Route path="/payment/error" render={(props) => <PaymentError {...props} />} />
      <Route path="/payment/failed" render={(props) => <PaymentFailed {...props} />} />
      <Route path="/" exact render={(props) => <HomePage {...props} />} />
    </Switch>
  );

  if (props.isAuthenticated) {
    routes = (
      <Switch>
        <Route path="/publication-requests" render={(props) => <PublicationRequests {...props} />} />
        <Route path="/create-publication-request" render={(props) => <CreatePublicationRequest {...props} />} />
        <Route path="/tasks" render={(props) => <Tasks {...props} />} />
        <Route path="/publication-request" render={(props) => <PublicationRequest {...props} />} />

        <Route path="/sign-out" render={(props) => <Logout {...props} />} />
        <Route path="/change-password" render={(props) => <ChangePassword {...props} />} />
        <Route path="/payment/success" render={(props) => <PaymentSuccess {...props} />} />
        <Route path="/payment/error" render={(props) => <PaymentError {...props} />} />
        <Route path="/payment/failed" render={(props) => <PaymentFailed {...props} />} />
        <Route path="/" exact render={(props) => <HomePage {...props} />} />
        <Route path="/shopping-cart" render={(props) => <ShoppingCart {...props} />} />
        <Redirect to="/" />
      </Switch>
    );
  }

  let toolbar = null;
  if (props.isAuthenticated) {
    toolbar = <CustomToolbar />;
  }
  return (
    <div>
      {toolbar}
      <Suspense fallback={<p>Loading...</p>}>{routes}</Suspense>
      <main>
        {props.children}
      </main>
    </div>
  );

}

const mapStateToProps = state => {
  return {
    isAuthenticated: state.signIn.isAuthenticated,
  }
};

const mapDispatchToProps = dispatch => {
  return {
    onTryAutoSignUp: () => dispatch(actions.authCheckState()),
  }
}

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(App));
