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
import signUpOptionsReducer from './components/Authentication/SignUpOptions/SignUpOptionsReducer';
import signUpReducer from './components/Authentication/SignUp/SignUpReducer';
import resetPasswordReducer from './components/Authentication/ResetPassword/ResetPasswordReducer';
import activateAccountReducer from './components/Authentication/ActivateAccount/ActivateAccountReducer';
import betaReaderGenresReducer from './components/Authentication/BetaReaderGenres/BetaReaderGenresReducer';
import homePageReducer from './components/HomePage/HomePageReducer';
import transactionReducer from './components/Payment/Transaction/TransactionReducer';
import createPublicationRequestReducer from './components/WriterPages/CreatePublicationRequest/CreatePublicationRequestReducer';
import tasksReducer from './components/Tasks/TasksReducer';
import publicationRequestReducer from './components/Tasks/Task/PublicationRequest/PublicationRequestReducer';
import editorChooseBetaReadersReducer from './components/Tasks/Task/EditorChooseBetaReaders/EditorChooseBetaReadersReducer';
import bookReducer from './components/BookDetails/BookDetailsReducer';
import editorDownloadDocumentReducer from './components/Tasks/Task/EditorDownloadDocument/EditorDownloadDocumentReducer';
import writerUploadDocumentReducer from './components/Tasks/Task/WriterUploadDocument/WriterUploadDocumentReducer';
import committeeVotingReducer from './components/Tasks/Task/MembershipProcess/CommitteeVoting/CommitteeVotingReducer';
import publRequestsReducer from './components/WriterPages/PublicationRequests/PublicationRequestsReducer';
import writerUploadWorkReducer from './components/Tasks/Task/MembershipProcess/WriterUploadWork/WriterUploadWorkReducer';
import membershipReducer from './components/Membership/MembershipReducer';
import userListReducer from './components/Authentication/ManageLecturersAndEditors/ManageLecturersAndEditorsReducer';
import opinionsOfBetaReadersReducer from './components/Tasks/Task/OpinionsOfBetaReaders/OpinionsOfBetaReadersReducer';
import opinionOfEditorReducer from './components/Tasks/Task/OpinionOfEditor/OpinionOfEditorReducer';
import publishingInfoReducer from './components/Tasks/Task/PublishingInfo/PublishingInfoReducer';
import subscriptionReducer from './components/Subscription/SubscriptionReducer';
import storage from 'redux-persist/lib/storage';
import { persistStore, persistReducer } from 'redux-persist';
import { PersistGate } from 'redux-persist/integration/react';
import merchantListReducer from './components/Authentication/ManageMerchants/ManageMerchantsReducer';
import purchasedBooksReducer from './components/PurchasedBooks/PurchasedBooksReducer';
import plagiarismComplaintReducer from './components/WriterPages/PlagiarismComplaint/PlagiarismComplaintReducer';
import assignReviewBoardReducer from './components/Tasks/Task/PlagiarismProcess/AssignReviewBoard/AssignReviewBoardReducer';
import editorsDownloadPlagiarismDocumentsReducer from './components/Tasks/Task/PlagiarismProcess/EditorDownloadPlagiarismDocuments/EditorDownloadPlagiarismDocumentsReducer';
import committeePlagiarismVoteReducer from './components/Tasks/Task/PlagiarismProcess/CommitteePlagiarismVote/CommitteePlagiarismVoteReducer';
import merchantBookListReducer from './components/MerchantBooks/MerchantBooksReducer';
import merchantBookReducer from './components/MerchantBooks/MerchantBookDetails/MerchantBookDetailsReducer';
import editorCheckIfBookIsPlagiarismReducer from './components/Tasks/Task/EditorCheckIfBookIsPlagiarism/EditorCheckIfBookIsPlagiarismReducer';
import transactionListReducer from './components/TransactionList/TransactionListReducer';

const rootReducer = combineReducers({
    toastr: toastrReducer,
    signIn: signInReducer,
    signUpOptions: signUpOptionsReducer,
    signUp: signUpReducer,
    resetPassword: resetPasswordReducer,
    activateAccount: activateAccountReducer,
    betaReaderGenres: betaReaderGenresReducer,
    homePage: homePageReducer,
    transaction: transactionReducer,
    createPublicationRequest: createPublicationRequestReducer,
    tasks: tasksReducer,
    publicationRequest: publicationRequestReducer,
    book: bookReducer,
    editorDownloadDocument: editorDownloadDocumentReducer,
    writerUploadDocument: writerUploadDocumentReducer,
    committeeVoting: committeeVotingReducer,
    writerUploadWork: writerUploadWorkReducer,
    memberships: membershipReducer,
    editorChooseBetaReaders: editorChooseBetaReadersReducer,
    publRequests: publRequestsReducer,
    userList: userListReducer,
    opinionsOfBetaReaders: opinionsOfBetaReadersReducer,
    opinionOfEditor: opinionOfEditorReducer,
    publishingInfo: publishingInfoReducer,
    subscription: subscriptionReducer,
    merchantList: merchantListReducer, 
    purchasedBooks: purchasedBooksReducer,
    plagiarismComplaint: plagiarismComplaintReducer,
    assignReviewBoard: assignReviewBoardReducer,
    editorsDownloadPlagiarismDocuments: editorsDownloadPlagiarismDocumentsReducer,
    committeePlagiarismVote: committeePlagiarismVoteReducer,
    merchantBookList:merchantBookListReducer,
    merchantBook:merchantBookReducer,
    editorCheckIfBookIsPlagiarism:editorCheckIfBookIsPlagiarismReducer,
    transactionList: transactionListReducer
});

const persistConfig = {
    key: 'root',
    storage,
};

//ako vam se camunda logika remeti, zakomentarisite ovo
//--
const appReducer = (state, action) => {
    if (action.type === 'SIGN_OUT') {
        state = undefined
      }
    
    return rootReducer(state, action)
}
const pReducer = persistReducer(persistConfig, appReducer);
//--

// const pReducer = persistReducer(persistConfig, rootReducer);

const store = createStore(pReducer,applyMiddleware(thunk));
const persistor = persistStore(store);

const app = (
    <Provider store={store}>
        <PersistGate loading={null} persistor={persistor}>
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
        </PersistGate>
    </Provider>

);

ReactDOM.render(app, document.getElementById('root'));
reportWebVitals();
