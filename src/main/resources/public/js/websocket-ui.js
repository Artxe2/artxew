const socket = {}
const path_param_regex = /\{\w+\}/g
const connection = el => {
	let uri = el.getAttribute("data-uri");
	const path_params = [...el.querySelectorAll("[data-path]")].map(v => [v, v.querySelector("[data-param]")])
	for (const [path, param] of path_params) {
		uri = uri.replace(path.dataset.path, param.value)
	}
	let is_open = uri in socket
	for (const [_, param] of path_params) {
		param.readOnly = !is_open
	}
	el.classList.toggle("_[data-open]/bdc=#ff6060;c=#ff6060")
	el.classList.toggle("_[data-execute]/block;mt=50px")
	if (is_open) {
		socket[uri].close();
		delete socket[uri]
		el.querySelector("[data-open]").innerText = "Open";
		el.querySelector("[data-transmit]").readOnly = true;
		el.querySelector("[data-receive]").value = "";
	} else {
		socket[uri] = new WebSocket("ws" + window.location.protocol.slice(4) + "//" + window.location.host + uri);
		socket[uri].addEventListener("message", e => {
			const receive = el.querySelector("[data-receive]");
			receive.value += (receive.value ? "\n" : "") + e.data
			receive.scrollTop = receive.scrollHeight
		})
		el.querySelector("[data-open]").innerText = "Close";
		el.querySelector("[data-transmit]").readOnly = false;
		el.querySelector("[data-transmit]").focus()
	}
}
const sendMessage = el => {
	let uri = el.getAttribute("data-uri")
	const path_params = [...el.parentNode.parentNode.querySelectorAll("[data-path]")].map(v => [v, v.querySelector("[data-param]")])
	for (const [path, param] of path_params) {
		uri = uri.replace(path.dataset.path, param.value)
	}
	socket[uri].send(el.querySelector("[data-transmit]").value);
}
document.addEventListener("readystatechange", () => {
	const endpoints = document.querySelector("[endpoints]")
	endpoints.innerHTML = `<!-- WEB SOCKETS START -->
	${json.map(ws => `
<div class=\"bg=#49cc901a m=0_0_15px bd=1px_solid_#49cc90 br=4px bs=0_0_3px_#00000030 c=#3b4151 >[foldable]/none\" data-uri=\"${ws.uri}\">
	<div class=\"bdb=1px_solid_#49cc90 flex ai=center p=5px pointer\" onclick=this.parentNode.classList.toggle('&gt;[foldable]/none')>
	<span class=\"bg=#49cc90 fs=14px bold mw=80px p=6px_15px ta=center br=3px ts=0_1px_0_#0000001a c=#fff\">WS</span>
	<span class=\"fs=16px flex ai=center wb=break-word p=0_10px ff=monospace bold c=#3b4151\">
		<a href=\"#${ws.uri}\">
		<span>${ws.uri}</span>
		</a>
	</span>
	<div class=\"fs=13px flex=1_1_auto wb=break-word ff=sans-serif c=#3b4151\">${ws.tag}</div>
	</div>
	<div foldable class=\"h=auto bd=none\">
	<div class=\"flex ai=center p=8px_20px bg=#ffffffcc bs=0_1px_2px_#00000019\">
		<div class=\"fs=14px flex=1 c=#3b4151\">
		<h4>Transmit Message</h4>
		</div>
		<button data-open class=\"fs=14px bold p=5px_23px tt=all_.3s bd=2px_solid_grey br=4px bg=transparent bs=0_1px_2px_#0000001a ff=sans-serif c=#3b4151 text-transform=none lh=1.15em o=visible appearance=button pointer ol=none c=#3b4151\" onclick=\"connection(this.parentNode.parentNode.parentNode)\">Open</button>
	</div>
	${path_param_regex.test(ws.uri)
		? `
	<div class="c=rgb(59,65,81)">
		<div class="p=20px">
			<table class="border-collapse=collapse p=0_10px">
				<thead>
					<tr>
						<th class="bdb=1px_solid_rgba(59,65,81,0.2) ff=sans-serif fs=12px p=12px_0 bold ta=left white-space=nowrap">Name</th>
						<th class="bdb=1px_solid_rgba(59,65,81,0.2) ff=sans-serif fs=12px p=12px_0 bold ta=left mb=2 w=100%">Description</th>
					</tr>
				</thead>
				<tbody>
					${path_param_regex.test(ws.uri), [...ws.uri.matchAll(path_param_regex)].map(value => `
					<tr data-path="${value[0]}">
						<td class="p=10px_0 va=top mw=6px white-space=nowrap">
							<div class="fs=16px bold mr=6px">${value[0].slice(1, value[0].length - 1)}<span class="c=red">&nbsp;*</span></div>
							<div class="ff=monospace fs=12px bold p=5px_0">string</div>
							<div class="c=red ff=monospace fs=12px font-style=italic bold"></div>
							<div class="c=gray ff=monospace fs=12px font-style=italic bold">(path)</div>
						</td>
						<td class="p=10px_0_0 va=top mb=2">
							<input data-param type="text" title="" placeholder="${value[0].slice(1, value[0].length - 1)}" value="" class="ff=sans-serif fs=16px lh=1 m=5px_0 o=visible bg=rgb(255,255,255) bdw=1px bds=solid bdc=rgb(217,217,217) br=4px mw=100px p=8px_10px xw=340px w=100%">
						</td>
					</tr>
					`)}
				</tbody>
			</table>
		</div>
	</div>`
		: ""}
	<div class=\"flex p=20px\">
		<div class=\"flex flex=1 column\" data-uri=\"${ws.uri}\">
			<textarea data-transmit class=\"resize=none fs=12px fw=bold p=10px bd=none br=4px ol=none bg=#41444e ff=monospace c=#fff flex=1 h=70px :not([readonly])/mh=210px;bg=#ffffffcc;c=#3b4151\" readonly></textarea>
			<button data-execute class=\"ff=sans-serif fs=14px hl=1.15 m=0 o=visible pointer ol=none bold p=8px_40px tt=all_0.3s_ease_0s; br=4px bg=#4990e2 c=#fff none\" onclick=\"sendMessage(this.parentNode)\">Execute</button>
		</div>
	</div>
	<div class=\"flex ai=center p=8px_20px bg=#ffffffcc bs=0_1px_2px_#0000001a\">
		<div class=\"fs=14px flex=1\">
			<h4>Receive Message</h4>
		</div>
	</div>
	<div class=\"flex p=20px\">
		<textarea data-receive class=\"resize=none fs=12px fw=bold p=10px bd=none br=4px ol=none bg=#41444e ff=monospace c=#fff flex=1 h=70px\" read-only></textarea>
	</div>
	</div>
</div>
`).join("")}
<!-- WEB SOCKETS END -->`
}, {
	once: 1
})