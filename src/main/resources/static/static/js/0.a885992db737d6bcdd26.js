webpackJsonp([0],{XwkR:function(t,e,i){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var a=i("mtWM"),s=i.n(a),n={data:function(){return{mediaList:[],userType:"1"}},created:function(){this.getMediaList(this.userType)},methods:{getMediaList:function(t){var e=this;this.mediaList=[],s.a.get("/media/getMediaList",{params:{userType:t+""}}).then(function(t){t.data.success&&e.mediaList.push.apply(e.mediaList,t.data.data.resultList)}).catch(function(t){})}}},r={render:function(){var t=this.$createElement,e=this._self._c||t;return e("div",[e("div",{staticStyle:{width:"500px",margin:"20px auto"}},[e("el-carousel",{attrs:{height:"300px"}},this._l(this.mediaList,function(t,i){return e("el-carousel-item",{key:i},[e("img",{attrs:{src:t.mediaUrl,width:"500px",height:"300px"}})])}))],1)])},staticRenderFns:[]};var u=i("VU/8")(n,r,!1,function(t){i("bj78")},"data-v-9faf25a2",null);e.default=u.exports},bj78:function(t,e){}});
//# sourceMappingURL=0.a885992db737d6bcdd26.js.map