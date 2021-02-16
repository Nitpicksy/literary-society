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
import BookDetails from './components/BookDetails/BookDetails';
import { GuardProvider, GuardedRoute } from 'react-router-guards';
import EditorDownloadDocument from './components/Tasks/Task/EditorDownloadDocument/EditorDownloadDocument';
import WriterUploadDocument from './components/Tasks/Task/WriterUploadDocument/WriterUploadDocument';
import WriterUploadWork from './components/Tasks/Task/MembershipProcess/WriterUploadWork/WriterUploadWork';
import CommitteeVoting from './components/Tasks/Task/MembershipProcess/CommitteeVoting/CommitteeVoting';
import Membership from './components/Membership/Membership';
import MerchantPaymentData from './components/MerchantPaymentData/MerchantPaymentData';
import LecturerAndEditorSignUp from './components/Authentication/LecturerAndEditorSignUp/LecturerAndEditorSignUp';
import ManageLecturersAndEditors from './components/Authentication/ManageLecturersAndEditors/ManageLecturersAndEditors';
import EditorChooseBetaReaders from './components/Tasks/Task/EditorChooseBetaReaders/EditorChooseBetaReaders';
import OpinionsOfBetaReaders from './components/Tasks/Task/OpinionsOfBetaReaders/OpinionsOfBetaReaders';
import OpinionOfEditor from './components/Tasks/Task/OpinionOfEditor/OpinionOfEditor';
import PublishingInfo from './components/Tasks/Task/PublishingInfo/PublishingInfo';
import Subscription from './components/Subscription/Subscription';
import SubscriptionSuccess from './components/Subscription/SubscriptionSuccess/SubscriptionSuccess';
import CancelSubscriptionSuccess from './components/Subscription/SubscriptionSuccess/CancelSubscriptionSuccess';
import MerchantSignUp from './components/Authentication/MerchantSignUp/MerchantSignUp';
import ManageMerchants from './components/Authentication/ManageMerchants/ManageMerchants';
import PurchasedBooks from './components/PurchasedBooks/PurchasedBooks';
import PlagiarismComplaint from './components/WriterPages/PlagiarismComplaint/PlagiarismComplaint';
import AssignReviewBoard from './components/Tasks/Task/PlagiarismProcess/AssignReviewBoard/AssignReviewBoard';
import EditorDownloadPlagiarismDocuments from './components/Tasks/Task/PlagiarismProcess/EditorDownloadPlagiarismDocuments/EditorDownloadPlagiarismDocuments';
import CommitteePlagiarismVote from './components/Tasks/Task/PlagiarismProcess/CommitteePlagiarismVote/CommitteePlagiarismVote';
import MerchantBooks from './components/MerchantBooks/MerchantBooks';
import MerchantBookDetails from './components/MerchantBooks/MerchantBookDetails/MerchantBookDetails';
import CreateBook from './components/MerchantBooks/CreateBook/CreateBook';
import ChoosePaymentMethods from './components/ChoosePaymentMethods/ChoosePaymentMethods';
import EditorCheckIfBookIsPlagiarism from './components/Tasks/Task/EditorCheckIfBookIsPlagiarism/EditorCheckIfBookIsPlagiarism';
import TransactionList from './components/TransactionList/TransactionList';
import SearchBooks from './components/SearchBooks/SearchBooks';
import ResultDetails from './components/Tasks/Task/EditorCheckIfBookIsPlagiarism/ResultDetails/ResultDetails';
// const Auth = React.lazy(() => {
//   return import('./containers/Auth/Auth');
// });

const App = props => {

  const { onTryAutoSignUp } = props;

  const roleWriter = "ROLE_WRITER";
  const roleReader = "ROLE_READER";
  const roleEditor ="ROLE_EDITOR";
  const roleCommitteeMember ="ROLE_COMMITTEE_MEMBER";
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

      <Route path="/editor/sign-up" render={(props) => <LecturerAndEditorSignUp {...props} />} />
      <Route path="/lecturer/sign-up" render={(props) => <LecturerAndEditorSignUp {...props} />} />
      <Route path="/merchant/sign-up" render={(props) => <MerchantSignUp {...props} />} />

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
      
      <Route path="/shopping-cart" render={(props) => <ShoppingCart {...props} />} />
      <Route path="/book/:id" exact render={(props) => <BookDetails {...props} />} />
      <Route path="/search" exact render={(props) => <SearchBooks {...props} />} />
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
          <Route path="/opinions-of-beta-readers" render={(props) => <OpinionsOfBetaReaders {...props} />} meta={{ roles: [roleWriter] }} />
          <Route path="/opinion-of-editor" render={(props) => <OpinionOfEditor {...props} />} meta={{ roles: [roleWriter] }} />
          
          <Route path="/check-if-book-is-plagiarism" render={(props) => <EditorCheckIfBookIsPlagiarism {...props} />} meta={{ roles: [roleEditor] }} />
          <Route path="/plagiarism-details" render={(props) => <ResultDetails {...props} />} meta={{ roles: [roleEditor] }} />

          <Route path="/publication-request" render={(props) => <PublicationRequest {...props} />} meta={{ roles: [roleEditor] }} />
          <Route path="/download-document" render={(props) => <EditorDownloadDocument {...props} />} meta={{ roles: [roleEditor, roleReader] }} />
          <Route path="/editor-choose-beta-readers" render={(props) => <EditorChooseBetaReaders {...props} />} meta={{ roles: [roleEditor] }} />
          <Route path="/publishing-info" render={(props) => <PublishingInfo {...props} />} meta={{ roles: [roleEditor] }} />

          <Route path="/writer-membership-upload" render={(props) => <WriterUploadWork {...props} />} meta={{ roles:  [roleWriter] }}/>
          <Route path="/voting" render={(props) => <CommitteeVoting {...props} />} meta={{ roles:  [roleWriter, roleCommitteeMember] }}/>
          
          <Route path="/membership" render={(props) => <Membership {...props} />} meta={{ roles:  [roleWriter, roleReader] }}/>
          <Route path="/subscription/cancel/success" render={(props) => <CancelSubscriptionSuccess {...props} />} meta={{ roles:  [roleWriter, roleReader] }}/>
          <Route path="/subscription/success" render={(props) => <SubscriptionSuccess {...props} />} meta={{ roles:  [roleWriter, roleReader] }}/>
          <Route path="/subscription" render={(props) => <Subscription {...props} />} meta={{ roles:  [roleWriter, roleReader] }}/>
          
          <Route path="/purchased-books" render={(props) => <PurchasedBooks {...props} />} meta={{ roles: [roleReader] }} />
          
          <Route path="/plagiarism-complaint" render={(props) => <PlagiarismComplaint {...props} />} meta={{ roles:  [roleWriter] }}/>
          <Route path="/plagiarism" render={(props) => <AssignReviewBoard {...props} />} meta={{ roles:  [roleEditor] }}/>
          <Route path="/plagiarism-review-download" render={(props) => <EditorDownloadPlagiarismDocuments {...props} /> } meta={{roles: [roleEditor]}} />
          <Route path="/plagiarism-vote" render={(props) => <CommitteePlagiarismVote {...props} /> } meta={{roles: [roleCommitteeMember]}} />

          <GuardedRoute path="/payment-data" render={(props) => <MerchantPaymentData {...props} />} meta={{ roles: [roleMerchant] }} />
          <GuardedRoute path="/merchant-books" render={(props) => <MerchantBooks {...props} />} meta={{ roles: [roleMerchant] }} />
          <GuardedRoute path="/merchant-book/:id" render={(props) => <MerchantBookDetails {...props} />} meta={{ roles: [roleMerchant] }} />
          <GuardedRoute path="/add-book" render={(props) => <CreateBook {...props} />} meta={{ roles: [roleMerchant] }} />

          <GuardedRoute path="/manage-users" render={(props) => <ManageLecturersAndEditors {...props} />} meta={{ roles: [roleAdmin] }} />
          <GuardedRoute path="/manage-merchants" render={(props) => <ManageMerchants {...props} />} meta={{ roles: [roleAdmin] }} />
          <GuardedRoute path="/choose-payment-methods" render={(props) => <ChoosePaymentMethods {...props} />} meta={{ roles: [roleAdmin] }} />
          <GuardedRoute path="/transactions" render={(props) => <TransactionList {...props} />} meta={{ roles: [roleAdmin] }} />

          <Route path="/tasks" render={(props) => <Tasks {...props} />} meta={{ roles: [roleEditor,roleWriter,roleReader, roleLecturer] }} />

          <Route path="/sign-out" render={(props) => <Logout {...props} />} />
          <Route path="/change-password" render={(props) => <ChangePassword {...props} />} />

          <Route path="/payment/success/:id" render={(props) => <PaymentSuccess {...props} />} />
          <Route path="/payment/error" render={(props) => <PaymentError {...props} />} />
          <Route path="/payment/failed" render={(props) => <PaymentFailed {...props} />} />

          <Route path="/shopping-cart" render={(props) => <ShoppingCart {...props} />} />
          <Route path="/book/:id" exact render={(props) => <BookDetails {...props} />} />
          <Route path="/search" exact render={(props) => <SearchBooks {...props} />} />
          
          <Route path="/error/non-authorized" render={(props) => <NonAuthorized {...props} />} />
          <Route path="/" exact render={(props) => <HomePage {...props} />} />

          <Redirect to="/" />
        </Switch>
      </GuardProvider>
    );
  }

  return (
    <div>
      <CustomToolbar role={props.role} isAuthenticated = {props.isAuthenticated}/>
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
