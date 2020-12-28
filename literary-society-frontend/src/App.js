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
import DocumentSubmission from './components/WriterPages/DocumentSubmission/DocumentSubmission';
import BookDetails from './components/BookDetails/BookDetails';
import { GuardProvider, GuardedRoute } from 'react-router-guards';
import EditorDownloadDocument from './components/Tasks/Task/EditorDownloadDocument/EditorDownloadDocument';
import WriterUploadDocument from './components/Tasks/Task/WriterUploadDocument/WriterUploadDocument';
import WriterUploadWork from './components/Tasks/Task/MembershipProcess/WriterUploadWork/WriterUploadWork';

// const Auth = React.lazy(() => {
//   return import('./containers/Auth/Auth');
// });

const App = props => {

  const { onTryAutoSignUp } = props;

  const roleWriter = "ROLE_WRITER";
  const roleReader = "ROLE_READER";
  const roleEditor ="ROLE_EDITOR";
  const roleAdmin ="ROLE_ADMIN";
  const roleLecturer="ROLE_LECTURER";
  const roleMerchant ="ROLE_MERCHANT";

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

      <Route path="/book/:id" exact render={(props) => <BookDetails {...props} />} />
      <Route path="/" exact render={(props) => <HomePage {...props} />} />
    </Switch>
  );

  if (props.isAuthenticated) {
    routes = (
      <GuardProvider guards={[requireRole]}>
        <Switch>
          <GuardedRoute path="/publication-requests" render={(props) => <PublicationRequests {...props} />} meta={{ roles: [roleWriter] }} />
          <GuardedRoute path="/create-publication-request" render={(props) => <CreatePublicationRequest {...props} />} meta={{ roles:  [roleWriter] }} />
          <Route path="/writer-upload-document" render={(props) => <WriterUploadDocument {...props} />} meta={{ roles: [roleWriter] }} />
        
          <Route path="/publication-request" render={(props) => <PublicationRequest {...props} />} meta={{ roles: [roleEditor] }} />
          <Route path="/editor-download-document" render={(props) => <EditorDownloadDocument {...props} />} meta={{ roles: [roleEditor] }} />

          <Route path="/upload" render={(props) => <WriterUploadWork {...props} />} meta={{ roles:  [roleWriter] }}/>

          <Route path="/tasks" render={(props) => <Tasks {...props} />} />
          <Route path="/sign-out" render={(props) => <Logout {...props} />} />
          <Route path="/change-password" render={(props) => <ChangePassword {...props} />} />

          <Route path="/payment/success" render={(props) => <PaymentSuccess {...props} />} />
          <Route path="/payment/error" render={(props) => <PaymentError {...props} />} />
          <Route path="/payment/failed" render={(props) => <PaymentFailed {...props} />} />

          <Route path="/shopping-cart" render={(props) => <ShoppingCart {...props} />} />
          <Route path="/book/:id" exact render={(props) => <BookDetails {...props} />} />

          <Route path="/error/non-authorized" render={(props) => <NonAuthorized {...props} />} />
          <Route path="/" exact render={(props) => <HomePage {...props} />} />

          <Redirect to="/" />
        </Switch>
      </GuardProvider>
    );
  }

  let toolbar = null;
  if (props.isAuthenticated) {
    toolbar = <CustomToolbar role={props.role} />;
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
    role: state.signIn.role,
  }
};

const mapDispatchToProps = dispatch => {
  return {
    onTryAutoSignUp: () => dispatch(actions.authCheckState()),
  }
}

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(App));
