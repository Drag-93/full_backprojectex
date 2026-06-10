const memberDetailFn=(event, id)=>{
    event.preventDefault();
    console.log(id)
    const url=`/api/admin/member/detail/${id}`;
    fetch(url)
        .then((res)=>res.json())
        .then((rs)=>{
            console.log(rs)
            const member=rs.member;

            document.querySelector('.userId').innerText=member.id;
            document.querySelector('.userEmail').innerText=member.userEmail;
            document.querySelector('.userPw').innerText=member.userPw;
            document.querySelector('.userName').innerText=member.username;
            document.querySelector('.role').innerText=member.role;
            document.querySelector('.createTime').innerText=member.createTime;
        })
        .catch((err)=>console.log(err));
    document.querySelector(".member_modal").classList.add("show");
};

const closeFn=()=>{
    document.querySelector(".member_modal").classList.remove("show");
}