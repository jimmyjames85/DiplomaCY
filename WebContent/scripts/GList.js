var GList = function()
{
	this.list = [];
	this.sorted = false;
	this.comparator = null;

	this.isSorted = function()
	{
		return this.sorted;
	};
	this.contains = function(element)
	{
		return this.indexOf(element) >= 0;
	};

	this.setComparator = function(comparator)
	{
		if (comparator)
		{
			this.comparator = comparator;
			this.sorted = true;
			this.sortWith(this.comparator);
		}
	};

	this.mergeSort = function(arr, comparator)
	{
		if (arr.length < 2 || (!comparator))
			return;
		var left = 0;
		var right = arr.length - 1;

		var stack = [];
		stack.push(
		[
				left, right
		]);

		while (stack.length != 0)
		{
			var next = stack.pop();
			left = next[0];
			right = next[1];

			if (left < 0 || right < 0)
			{
				left = -left;
				right = -right;
				var mid = Math.floor((right + left) / 2);
				var i = left;
				var j = mid + 1;
				var tmpArr = [];
				while (i <= mid && j <= right)
				{
					var c = comparator(arr[i], arr[j]);
					if (c >= 0)
					{
						tmpArr.push(arr[j]);
						j++;
					}
					else
					{
						tmpArr.push(arr[i]);
						i++;
					}
				}
				while (i <= mid)
				{
					tmpArr.push(arr[i]);
					i++;
				}
				while (j <= right)
				{
					tmpArr.push(arr[j]);
					j++;
				}

				i = 0;
				while (i < tmpArr.length)
				{
					arr[left] = tmpArr[i];
					i++;
					left++;
				}
				tmpArr = [];
				delete tmpArr;
			}
			else if (left == right)
			{
				continue;
			}
			else if (right == left + 1)
			{
				var c = comparator(arr[left], arr[right]);
				if (c >= 0)
				{
					var tmp = arr[left];
					arr[left] = arr[right];
					arr[right] = tmp;
					continue;
				}
			}
			else
			{
				var mid = Math.floor((right + left) / 2);
				stack.push(
				[
						-left, -right
				]);//negative left and right indicates a merge should be performed
				stack.push(
				[
						left, mid
				]);
				stack.push(
				[
						mid + 1, right
				]);
			}
		}
	}

	this.sortWith = function(comparator)
	{
		var left = 0;
		var right = this.list.length - 1;
		if (left <= right)
			this.mergeSort(this.list, comparator);

		/*		while (right < this.list.size - 1)
		{
			var d = this.binFind(this.list, left, right, this.list[right + 1], comparator);
			//indexOf(this.list[right]);

		}*/
	}

	this.add = function(element)
	{
		if (this.sorted && this.list.length != 0)
		{
			var index = this.binFind(this.list, 0, this.list.length - 1, element, this.comparator);
			if (index < 0)
				index = -index - 1;

			this.list.splice(index, 0, element);
		}
		else
			this.list.push(element);
	};

	/**
	 * returns the index of the elemToFind within the arr from index left to right(inclusive)
	 * 
	 * If not found a negative index, i is returned where arr[-i-1] is where the elemToFind should be
	 */
	this.binFind = function(arr, left, right, elemToFind, cmp)
	{
		if (left > right)
			throw "invalid right<left";

		if (arr.length == 0)
			throw "empty array";

		while (left <= right)
		{
			var mid = Math.floor((right + left) / 2);
			if (mid == left || mid == right)
			{
				//down to two elements, check them both
				var cmpLeft = cmp(elemToFind, arr[left]);
				var cmpRight = cmp(elemToFind, arr[right]);
				if (cmpLeft == 0)
					return left;
				else if (cmpRight == 0)
					return right;

				if (cmpLeft < 0)
					return -1 * (left + 1);
				else if (cmpRight > 0)
					return -1 * (right + 2);
				else
					return -1 * (mid + 1);
			}
			var c = cmp(elemToFind, arr[mid]);
			if (c == 0)
				return mid;
			else if (c < 0)
				right = mid - 1;
			else
				left = mid + 1;
		}

		throw "should never reach here";
		//return -1;//should never reach here
	};

	/**
	 * returns the index of the elemToFind within the arr from index left to right(inclusive)
	 * 
	 * If not found a negative index, i is returned where arr[-i-1] is where the elemToFind should be
	 */
	this.indexOf = function(elemToFind)
	{
		var index = -1;
		if (this.sorted && this.list.length > 1)
		{
			index = this.binFind(this.list, 0, this.list.length - 1, elemToFind, this.comparator);
		}
		else
		{
			var found = false;
			for (var i = 0; !found && i < this.list.length; i++)
			{
				if (this.list[i] == elemToFind)
				{
					found = true;
					index = i;
				}
			}
		}
		return index;
	}

	this.remove = function(element)
	{
		var indexOfElem = this.indexOf(element);
		var elem = null;
		if (indexOfElem >= 0)
		{
			elem = this.get(indexOfElem);
			this.list.splice(indexOfElem, 1);
		}
		return elem;
	};

	this.removeAllElements = function()
	{
		while (this.list.length > 0)
			this.list.splice(0, 1);
	};

	this.size = function()
	{
		return this.list.length;
	};

	this.get = function(i)
	{
		return this.list[i];
	};

	this.length = function()
	{
		return this.list.length;
	};

}