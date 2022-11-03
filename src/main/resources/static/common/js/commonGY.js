/***************** START sweetAlert JS *****************/
/*
 * @param
 * msg : 알림 메세지,
 * titie : 알림창 제목
 * btnText : 버튼이름
 * icon: 알림창 유형 (warning,error,success,info)
 */
const alert = (msg, title, btnText, icon) => {
    title =  empty(title) == '' ? '' : title;
    icon = empty(icon) == '' ? '' : type;
    btnText = empty(btnText) == '' ? '확인' : type;

    swal({
        title: title,
        text: msg,
        icon: icon,
        button: btnText,
    });
}

/*
 * @param
 * msg : 알림 메세지,
 * titie : 알림창 제목
 * icon: 알림창 유형 (warning,error,success,info)
 */
// TODO 에러발생 확인 필요 2022.11.02
const confirm = (msg, callbackMsg, title, BtnConfirmMsg, BtnCancelMsg, icon) => {
    title =  empty(title) == '' ? '' : title;
    callbackMsg =  empty(callbackMsg) == '' ? '' : BtnCancelMsg;
    BtnConfirmMsg = empty(BtnConfirmMsg) == '' ? '' : BtnConfirmMsg;
    BtnCancelMsg =  empty(BtnCancelMsg) == '' ? '' : BtnCancelMsg;
    icon = empty(icon) == '' ? 'warning' : type;

    swal({
      title: title,
      text: msg,
      icon: icon,
      buttons: true,
      dangerMode: true
//      confirmButtonText : "예",
//      BtnCancelMsg : "아니요",
    })
    .then((willDelete) => {
      if (willDelete) {
        swal(callbackMsg, {
          icon: "success",
        });
      }
    });
}
/***************** END sweetAlert JS *****************/


/*
 * @param : String
 * @설명 : 공백확인
 */
function empty(str){
    if(typeof str == "undefined" || str == null || str == "")
        return '';
    else
        return str ;
}