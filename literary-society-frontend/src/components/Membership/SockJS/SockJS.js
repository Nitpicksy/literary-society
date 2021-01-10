import React, { useRef } from 'react';
import SockJsClient from 'react-stomp';
import { toastr } from 'react-redux-toastr';

const SockJS = () => {

    let ref = useRef(null)

    const handleMessage = (url) => {
        if(url.length > 0) {
                window.location.href = url;
        } else {
            toastr.error("Oops", "Something went wrong with our payment system.")
        }
    }

    return (
        <SockJsClient url='https://localhost:8090/ws' topics={['/socket-publisher']}
        onMessage={(msg) => handleMessage(msg)}
        ref={ (client) => { ref = client }} />
    )
}

export default SockJS;