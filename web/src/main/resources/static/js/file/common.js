Vue.component("footer-component", {
    props: ['post'],

    template: `
        <div style="display: flex;justify-content: center;margin-top: 5%;background-color: #d7d7d7">
            <ul class="nav">
                <li><a class="nav-link text-secondary" data-toggle="modal" data-target="#exampleModal" href="#">版权声明</a></li>
            </ul>
<div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLabel">版权声明</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
        <ol>
            <li>简约派资源网，所提供的资源，来源于 互联网收集+本站原创+素材购买。</li>
            <li>对于简约派资源网的用户：虽然您在本站可以找到这些资源，但除了可以在网上浏览或下载之外，我们并未授权您将这些资源用于其它任何商业用途。</li>
            <li>简约派资源网 所提供的资源均为免费自由下载，目的是让广大资源爱好者学习研究和交流。</li>
            <li>如果本站的资源使用了您的作品，<strong>请联系我们，我们会及时的注明。</strong></li>
            <li>如果您的作品不愿在本站展示，<strong>请联系我们，我们会及时删除。</strong></li>
            <li>本站资源免费下载，仅供学习研究使用。如果因为您将本站资源用于其他用途而引起的纠纷，本站不负任何责任。</li>
            <li>联系方式 qq邮箱： 1161889163@qq.com</li>
        </ol>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">关闭</button>
      </div>
    </div>
  </div>
</div>
        </div>
    `
})
new Vue({
    el: "#footer"
})