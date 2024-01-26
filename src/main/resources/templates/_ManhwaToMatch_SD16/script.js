 // logout 共通モーダル
 function openModal() {
    var modal = document.getElementById("lo");
    modal.style.display = "block";
  }

  function closeModal() {
    var modal = document.getElementById("lo");
    modal.style.display = "none";
  }

  function logout() {
    // ログアウト処理がここに入ります
    alert("ログアウトしました");
    closeModal();
  }


//BookSearch.html
function executeGenreSearch() {
  var selectedGenres = [];
  var checkboxes = document.querySelectorAll('.inputs');
  checkboxes.forEach(function(checkbox) {
    if (checkbox.checked) {
      selectedGenres.push(checkbox.value);
    }
  });

  // ここでジャンル検索のロジックを実行する
  // ...
}

function selectAllGenres() {
  var checkboxes = document.querySelectorAll('.inputs');
  checkboxes.forEach(function(checkbox) {
    checkbox.checked = true;
  });
}

function resetGenres() {
  var checkboxes = document.querySelectorAll('.inputs');
  checkboxes.forEach(function(checkbox) {
    checkbox.checked = false;
  });

  const all作品 = document.querySelectorAll('.作品');
  all作品.forEach(作品 => {
    作品.style.display = 'block';
  });

  // 検索結果一覧を非表示にして、デフォルトの「作品一覧」に戻す
  var resultsElement = document.getElementById('results');
  resultsElement.textContent = 'マンガ作品一覧';
  resultsElement.style.display = 'block'; // 表示を block に設定する

  var checkboxes = document.querySelectorAll('.inputs');
  checkboxes.forEach(function(checkbox) {
    checkbox.checked = false;
  });
  var searchInput = document.getElementById('searchInput');
  searchInput.value = '';
}


function randomSelection() {
  var checkboxes = document.querySelectorAll('.inputs');
  var numToCheck = Math.floor(Math.random() * checkboxes.length) + 1; // ランダムな数のチェックボックスを選択

  var count = 0;
  checkboxes.forEach(function(checkbox) {
    checkbox.checked = false; // すべてのチェックボックスをリセット
  });

  while (count < numToCheck) {
    var randomIndex = Math.floor(Math.random() * checkboxes.length);
    if (!checkboxes[randomIndex].checked) {
      checkboxes[randomIndex].checked = true; // ランダムなチェックボックスをチェック
      count++;
    }
  }
}

function filter() {
  var searchValue = document.getElementById('searchInput').value.trim().toLowerCase();
  var checkboxes = document.querySelectorAll('input[type="checkbox"]');
  var selectedGenres = Array.from(checkboxes).filter(function (checkbox) {
      return checkbox.checked;
  }).map(function (checkbox) {
      return checkbox.value;
  });

  if (searchValue.length > 0 && searchValue.length < 2) {
      alert("検索キーワードは2文字以上で入力してください。");
      return;
  }

   // 検索結果一覧の表示
  
   document.getElementById('results').textContent = '検索結果';
   document.getElementById('results').style.display = 'block';


   var all作品 = document.querySelectorAll('.作品');
   var resultsFound = false;
   all作品.forEach(function (作品) {
       var title = 作品.getAttribute('data-title').toLowerCase();
       var author = 作品.querySelector('p:nth-of-type(1)').textContent.toLowerCase().split(": ")[1];
       var genre = 作品.getAttribute('data-genre');
       if ((title.includes(searchValue) || author.includes(searchValue)) && isGenreSelected(genre, selectedGenres)) {
           作品.style.display = 'block';
           resultsFound = true;
       } else {
           作品.style.display = 'none';
       }
   });
 
   if (!resultsFound) {
       // 検索結果が見つからなかったときの処理
       document.getElementById('results').textContent = '条件に合う作品が見つかりませんでした';
   } else {
       document.getElementById('results').textContent = '検索結果 作品一覧';
   }
}

function isGenreSelected(genre, selectedGenres) {
  if (selectedGenres === undefined || selectedGenres.length === 0) {
      return true;
  }
  return selectedGenres.includes(genre);
}


      function sort() {
        const sortValue = document.getElementById('sort').value;
        const 作品ラッパー = document.getElementById('作品ラッパー');
        const all作品 = [...作品ラッパー.children];
        let sorted作品;
      
        if (sortValue === 'title-ascending') {
          sorted作品 = all作品.sort((a, b) => {
            const titleA = a.getAttribute('data-title');
            const titleB = b.getAttribute('data-title');
            return titleA.localeCompare(titleB, 'ja');
          });
        } else if (sortValue === 'title-descending') {
          sorted作品 = all作品.sort((a, b) => {
            const titleA = a.getAttribute('data-title');
            const titleB = b.getAttribute('data-title');
            return titleB.localeCompare(titleA, 'ja');
          });
        } else if (sortValue === 'author-ascending') {
          sorted作品 = all作品.sort((a, b) => {
            const authorA = a.querySelector('p:nth-of-type(1)').textContent.split(": ")[1];
            const authorB = b.querySelector('p:nth-of-type(1)').textContent.split(": ")[1];
            return authorA.localeCompare(authorB, 'ja');
          });
        } else if (sortValue === 'author-descending') {
          sorted作品 = all作品.sort((a, b) => {
            const authorA = a.querySelector('p:nth-of-type(1)').textContent.split(": ")[1];
            const authorB = b.querySelector('p:nth-of-type(1)').textContent.split(": ")[1];
            return authorB.localeCompare(authorA, 'ja');
          });
        } else   if (sortValue === 'rating-ascending') {
        sorted作品 = all作品.sort((a, b) => {
          const ratingA = parseFloat(a.getAttribute('data-rating'));
          const ratingB = parseFloat(b.getAttribute('data-rating'));
          return ratingB - ratingA;
        });
      }

      作品ラッパー.innerHTML = '';
      sorted作品.forEach(作品 => {
        作品ラッパー.appendChild(作品);
      });
    }

    


    