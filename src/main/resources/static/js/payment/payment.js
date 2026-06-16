const payResult = document.querySelector('#payResult')
const paymentType=document.querySelector('#paymentType')
const cartId=document.querySelector('#cartId')
const memberId=document.querySelector('#memberId')
const orderPost=document.querySelector('#orderPost')
const orderMethod=document.querySelector('#orderMethod')
const orderAddr=document.querySelector('#orderAddr')

const paymentFn=(event)=>{
    event.preventDefault();
    const url = `/payment/insert2`;
    const itemData={
        cartId:cartId.innerText,
        orderPost:orderPost.value,
        orderMethod:orderMethod.value,
        payResult:payResult.innerText,
        orderAddr:orderAddr.value,
        memberId:memberId.innerText,
        paymentType:paymentType.value
    }
    fetch(url,{
        method:'POST',
        headers:{
            'Content-Type':'application/json'
        },
        body:JSON.stringify(itemData)
    })
        .then(res=>res.json())
        .then(rs=>{
            console.log(rs)
            //정상 처리 ->
            if(rs==1){
                location.href = `/payment/paymentList/${memberId.innerText}`;
            }
            //에러 -> 그대로
        })
        .catch(err=>console.log(err));
    alert("결제 실행")
}