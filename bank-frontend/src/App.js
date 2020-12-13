import { Redirect, Route, Switch, withRouter} from 'react-router-dom';
import './App.css';
import EnterCreditData from './components/EnterCreditCardData/EnterCreditCardData';

const App = props => {

  let routes = (
    <Switch>
      <Route path="/payment/confirm/:id" render={(props) => <EnterCreditData {...props} />} />
      <Redirect to="/" />
    </Switch>
  );

  return (
    <div className="App">
      {routes}
      <main>
          {props.children}
        </main>
    </div>
  );
}

export default withRouter(App);
