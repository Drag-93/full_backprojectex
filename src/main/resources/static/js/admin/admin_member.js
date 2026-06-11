const memberDetailFn=(event, id)=>{
    event.preventDefault();
    console.log(id)
    const url=`/api/admin/member/detail/${id}`;
    fetch(url)
        .then((res)=>res.json())
        .then((rs)=>{
            console.log(rs)
            const member=rs.member;

            document.querySelector('.userId2').innerText=member.id;
            document.querySelector('.userEmail2').innerText=member.userEmail;
            document.querySelector('.userPw2').innerText=member.userPw;
            document.querySelector('.userName2').innerText=member.userName;
            document.querySelector('.role2').innerText=member.role;
            document.querySelector('.createTime2    ').innerText=member.createTime;

            document.querySelector(".member_modal").classList.add("show");

        })
        .catch((err)=>console.log(err));
};

const closeFn=()=>{
    document.querySelector(".member_modal").classList.remove("show");
}