import React, { useEffect } from 'react';
import { Redirect } from 'react-router-dom';
import { connect } from 'react-redux';
import * as actions from './SignInExport';

const Logout = props => {
    const {onLogout} = props;
    
    useEffect(() => {
        onLogout();
    }, [onLogout]);

    return <Redirect to="/sign-in"/>;
}

const mapDispatchToProps = dispatch => {
    return {
        onLogout: () => dispatch(actions.signOut())
    }
};

export default connect(null, mapDispatchToProps)(Logout);