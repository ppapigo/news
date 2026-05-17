//Rest api backend 서버 주소
const API_BASE = 'http://localhost:8091'
const FALLBACK_IMG='images/news.jpg'

//api 서버로부터 카테고리 목록 가져오기
async function getCategories(){
    const response = await fetch(API_BASE + '/api/news/categories');
    const data = await response.json();
    return data.categories;
}

//api 서버로부터 뉴스기사 목록 가져오기
async function getArticles(category) {
    const url = API_BASE + '/api/news/?category=' + category
    const response = await fetch(url);
    const data =await response.json();

    return data.articles;
    
}

//카테고리 탭 페이지에 노출하기
function showTabs(categories,selected){
    //DOM 오브젝트를 찾아서 할당
    const tabsDiv = document.getElementById('tabs');
    let html='';
    for(let i=0; i<categories.length; i++){
        const cat= categories[i];
        const isActive=cat.name === selected ? 'active':'';
        html += '<a class="tab'+
        '"href="#" data-category="'+
        cat.name + 
        '">' + 
        cat.name + '</a>';

    
    }
    tabsDiv.innerHTML = html;

    //class 이름으로 DOM Object찾기
    const tabs = document.querySelectorAll('.tab');
    for(let i=0; i<tabs.length; i++){
        tabs[i].addEventListener('click', function(event){
            event.preventDefault();
            const category =this.getAttribute('data-category');
            showArticles(category);
            updateActivateTab(category);
        });
    }
}

function updateActivateTab(selected){
    const tabs = document.querySelectorAll('.tab');
    for(let i=0; i<tabs.length; i++){
        tabs[i].classList.remove('active');
        if(tabs[i].getAttribute('data-category')===selected){
            tabs[i].classList.add('active');
        }
    }
}

//기사들을 grid 섹션에 노출시키기
async function showArticles(category){
    const gridDiv = document.getElementById('grid');


    const articles = await getArticles(category);

    if(!articles || articles.length === 0){
        gridDiv.innerHTML='';
        gridDiv.style.display='none';
        return;
    }

    gridDiv.style.display='';
    let html = '';

    for(let i =0; i<articles.length; i++){
        const article=articles[i];
        const imageUrl = article.urlToImage || FALLBACK_IMG;
        const date = article.publishedAt;
        const source = article.source ? article.source.name : '-';
        const title = article.title || '제목없음';
        const desc = article.description || '';
        const url = article.url || '#';

        html += '<div class="card">';
        html += '<img class="thumb" src="' +imageUrl+'"alt="news thumbnail" onerror="this.src=\''+FALLBACK_IMG+'\';">'
        html += '<div class="card-body">';
        html += '<h2><a href="' +url+'" target="_blank">'+title +'</a></h2>';

        if(desc){
            html +='<p class="desc">' + desc + '</p>';
        }

        html += '<div class="meta">';
        html += '<span class="source">' +source+ '</span>';
        html += '<span>' +date + '</span>';
        html += '</div>' //meta
        html += '</div>' //card-body
        html += '</div>' //card
    }

    gridDiv.innerHTML = html;
}

function getCategoryFromUrl() {
    const urlParams = new URLSearchParams(window.location.search);
    const category = urlParams.get('category');
    return category || 'business';
}

async function init() {
    if(!window.location.search.includes('category')){
        window.location.href = '?category=business';
        return;
    }
    
    const selectedCategory = getCategoryFromUrl();
    const categories = await getCategories();

    showTabs(categories,selectedCategory);
    await showArticles(selectedCategory);
}

document.addEventListener('DOMContentLoaded', init);