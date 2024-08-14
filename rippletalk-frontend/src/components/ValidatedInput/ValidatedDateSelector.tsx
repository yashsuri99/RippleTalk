import React, { useEffect, useState } from 'react';
import { determineValidatedSelectStyle } from '../../utils/DetermineStylesUtils';
import { StyledInputBox, StyledInputLabel } from './StyledInput';

interface ValidatedDateSelectorProps {
	style: string;
	valid: boolean;
	name: string;
	dropdown(): JSX.Element[];
	dispatcher(name: string, value: string | number | boolean): void;
}

export const ValidatedDateSelector: React.FC<ValidatedDateSelectorProps> = ({
	style,
	valid,
	name,
	dropdown,
	dispatcher,
}) => {
	const [active, setActive] = useState<boolean>(false);
	const [value, setValue] = useState<number>(0);
	const [color, setColor] = useState<string>('gray');

	const changeValue = (e: React.ChangeEvent<HTMLSelectElement>) => {
		setValue(+e.target.value);
		dispatcher(name.toLowerCase(), +e.target.value);
	};

	const toggleActive = (e: React.FocusEvent<HTMLSelectElement>) => {
		setActive(!active);
	};

	useEffect(() => {
		setColor(determineValidatedSelectStyle(active, valid));
	}, [active, valid, value]);

	return (
		<div className={style}>
			<StyledInputBox active={active} valid={valid}>
				<StyledInputLabel color={color} active={active} valid={valid}>
					{name}
				</StyledInputLabel>
				<select
					onChange={changeValue}
					onFocus={toggleActive}
					onBlur={toggleActive}
				>
					{dropdown()}
				</select>
			</StyledInputBox>
		</div>
	);
};
