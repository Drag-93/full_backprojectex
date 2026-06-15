//openChat() 채팅 팝업 열기 + 웹소켓 연결 시도
const chatDisp = document.querySelector('#chat-disp');
const chatContent = document.querySelector('#chat-content');
const question = document.querySelector('#question');
let stompClient=null;








//채팅창 열기
const openChat=()=>{
    console.log('openChat');
    chatDisp.classList.add('show');
    onconnect();
};

//STOMP 접속
const onconnect=()=>{
    alert("접속");
    //1. 웹소켓 연결(SockJS)
    const socket=new SockJS('/chatEndpoint'); // 엔드포인트 주소
    //2. STOMP클라이언트 생성
    stompClient = Stomp.over(socket);
    //3. 서버 연결
    stompClient.connect({},(frame)=>{
        console.log("Connected:",frame);
        //최초 연결 시 서버에 인사 메시지 전송
        stompClient.send("/app/hellow",{},JSON.stringify({content: 'GUEST11'}));
        //서버 응답 구독
        stompClient.subscribe("/topic/greetings",(message)=>{
            const body=JSON.parse(message.body);
            showMessageFn(body.message);
        });
        stompClient.subscribe("/topic/message",(message)=> {
            const body = JSON.parse(message.body);
            showMessageFn(body.message);
        });
        //rabbitmq구독
        stompClient.subscribe("/topic/question", (res) => {
            const answer = JSON.parse(res.body);
            showMessageFn(answer.message);
        });
    },(error)=>{
        console.error('STOMP 연결 실패:',error)
        alert("서버 연결 실패")
    });
};

//질문 전송
const msgSendClickFn=()=>{
    const inputVal=question.value.trim();
    if(inputVal.length===0){
        alert("질문 내용을 입력해주세요");
        question.focus();
        return;
    }
    //1. 사용자의 질문 메시지 바로 표시
    const questionHTML=questionString(inputVal);
    showMessageFn(questionHTML);
    //2. 서버에 메시지 전송
    stompClient.send("/app/message",{},JSON.stringify({content:inputVal}));
    //입력창 초기화 및 포커스
    question.value='';
    question.focus();
    //채팅창 맨 아래로 스크롤
    chatContent.scrollTop=chatContent.scrollHeight;
};
//사용자 질문 메시지 HTML 생성
const questionString=(data)=>{
    const time = new Date().toLocaleString();
    return `<div class="questionString">
            <div class="question-data">${data}</div>
            <div class="data-time">${time}</div></div>`;
};
//채팅 메시지 화면 출력
const showMessageFn=(message)=>{
    const divTag=document.createElement('div');
    divTag.classList.add('data-con');
    divTag.innerHTML=message;
    chatContent.appendChild(divTag);
    //스크롤 맨 아래로 이동
    chatContent.scrollTop=chatContent.scrollHeight;
};
//연결 해제
const disconnect=()=> {
    if (stompClient) {
        stompClient.disconnect(() => {
            console.log("Disconnected");
        });
        stompClient = null;
    }
    chatDisp.classList.remove('show');
    chatContent.innerHTML = '';
};


//예비 함수(미사용)
const rabbitMsgSendClickFn=()=>{
    //추가 예정
    const inputVal=question.value.trim();
    if(inputVal.length===0){
        alert("질문 내용을 입력해주세요");
        question.focus();
        return;
    }
    //1. 사용자의 질문 메시지 바로 표시
    const questionHTML=questionString(inputVal);
    showMessageFn(questionHTML);
    //2. 서버에 메시지 전송
    stompClient.send("/app/bot", {}, JSON.stringify({
        content: inputVal
    }));

    //입력창 초기화 및 포커스
    question.value='';
    question.focus();
    //채팅창 맨 아래로 스크롤
    chatContent.scrollTop=chatContent.scrollHeight;
};














