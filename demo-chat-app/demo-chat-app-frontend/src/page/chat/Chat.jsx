import React, { useContext } from "react";
import { ChatEngine } from "react-chat-engine";
import Context from "../../app/context/Context";

function Chat() {
    const { auth } = useContext(Context);
    // console.log(auth);
    // console.log(auth.username);
    // console.log(auth.secret);
    return (
        <div>
            <ChatEngine
                projectID='44e33f55-3b67-422d-9b68-34b148e4bbf8'
                userName={auth.username}
                userSecret={auth.secret}
            />
        </div>
    );
}

export default Chat;
